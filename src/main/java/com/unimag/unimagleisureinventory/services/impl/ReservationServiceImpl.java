package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.reservation.ApproveReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.CreateReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.ReservationResponseDTO;
import com.unimag.unimagleisureinventory.exceptions.BusinessException;
import com.unimag.unimagleisureinventory.exceptions.ResourceNotFoundException;
import com.unimag.unimagleisureinventory.exceptions.UnauthorizedException;
import com.unimag.unimagleisureinventory.mappers.ReservationMapper;
import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;
import com.unimag.unimagleisureinventory.model.item.Item;
import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.model.reservation.Reservation;
import com.unimag.unimagleisureinventory.repositories.ItemRepository;
import com.unimag.unimagleisureinventory.repositories.ReservationRepository;
import com.unimag.unimagleisureinventory.repositories.StudentRepository;
import com.unimag.unimagleisureinventory.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    private final ItemRepository itemRepository;
    private final ReservationMapper reservationMapper;
    private final AuditLogServiceImpl auditLogService;

    public ReservationResponseDTO getById(UUID id) {
        return reservationMapper.toResponseDTO(
                reservationRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"))
        );
    }

    @Transactional
    public ReservationResponseDTO create(CreateReservationRequestDTO request, Long studentId) {

        // RF-07 — verificar que no tenga préstamo activo
        boolean hasActive = reservationRepository.existsByStudentIdAndStatus(
                studentId, ReservationStatus.ACCEPTED);
        if (hasActive) {
            throw new BusinessException("Student already has an active loan");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));

        if (item.getAvailableQuantity() <= 0) {
            throw new BusinessException("Item not available");
        }

        Reservation reservation = new Reservation();
        reservation.setStudent(student);
        reservation.setItem(item);
        reservation.setReservationTime(request.reservationTime());
        reservation.setClaimDeadline(request.reservationTime().plusMinutes(30)); // configurable RF-02
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationMapper.toResponseDTO(reservationRepository.save(reservation));
    }

    // RF-10 — consultar reservas activas del estudiante
    public List<ReservationResponseDTO> getMyReservations(Long studentId) {
        return reservationRepository.findByStudentId(studentId)
                .stream()
                .map(reservationMapper::toResponseDTO)
                .toList();
    }

    // RF-11 — cancelar reserva
    @Transactional
    public void cancel(UUID reservationId, Long studentId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (!reservation.getStudent().getStudentId().equals(studentId)) {
            throw new UnauthorizedException("Unauthorized");
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new BusinessException("Only pending reservations can be cancelled");
        }

        ReservationStatus previous = reservation.getStatus();
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
        auditLogService.logReservationStatus(reservation, previous, ReservationStatus.CANCELLED);
    }

    // RF-12 — verificar reserva por auxiliar
    public ReservationResponseDTO verify(UUID reservationId) {
        return reservationMapper.toResponseDTO(
                reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"))
        );
    }

    // RF-13 — aprobar o rechazar entrega
    @Transactional
    public ReservationResponseDTO approve(UUID reservationId, ApproveReservationRequestDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new BusinessException("Only pending reservations can be approved");
        }

        ReservationStatus previous = reservation.getStatus(); // captura PENDING

        if (request.approved()) {
            reservation.setStatus(ReservationStatus.ACCEPTED);
            itemRepository.decrementAvailableQuantity(reservation.getItem().getItemId());
        } else {
            reservation.setStatus(ReservationStatus.REJECTED);
        }

        Reservation saved = reservationRepository.save(reservation);
        auditLogService.logReservationStatus(saved, previous, saved.getStatus());

        return reservationMapper.toResponseDTO(saved);
    }
}
