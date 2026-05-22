package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.reservation.ApproveReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.CreateReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.ReservationResponseDTO;
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

    @Transactional
    public ReservationResponseDTO create(CreateReservationRequestDTO request, Long studentId) {

        // RF-07 — verificar que no tenga préstamo activo
        boolean hasActive = reservationRepository.existsByStudent_IdAndStatus(
                studentId, ReservationStatus.ACCEPTED);
        if (hasActive) {
            throw new RuntimeException("Student already has an active loan");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Item not available");
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
        return reservationRepository.findByStudent_Id(studentId)
                .stream()
                .map(reservationMapper::toResponseDTO)
                .toList();
    }

    // RF-11 — cancelar reserva
    @Transactional
    public void cancel(UUID reservationId, Long studentId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getStudent().getStudentId().equals(studentId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Only pending reservations can be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    // RF-12 — verificar reserva por auxiliar
    public ReservationResponseDTO verify(UUID reservationId) {
        return reservationMapper.toResponseDTO(
                reservationRepository.findById(reservationId)
                        .orElseThrow(() -> new RuntimeException("Reservation not found"))
        );
    }

    // RF-13 — aprobar o rechazar entrega
    @Transactional
    public ReservationResponseDTO approve(UUID reservationId, ApproveReservationRequestDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Only pending reservations can be approved");
        }

        if (request.approved()) {
            reservation.setStatus(ReservationStatus.ACCEPTED);
            // RF-14 — actualizar inventario
            itemRepository.decrementAvailableQuantity(reservation.getItem().getItemId());
        } else {
            reservation.setStatus(ReservationStatus.REJECTED);
        }

        return reservationMapper.toResponseDTO(reservationRepository.save(reservation));
    }
}
