package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.checkout.CheckInRequestDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CheckOutResponseDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CreateCheckOutRequestDTO;
import com.unimag.unimagleisureinventory.mappers.CheckOutMapper;
import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
import com.unimag.unimagleisureinventory.model.enums.ItemCondition;
import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;
import com.unimag.unimagleisureinventory.model.item.Item;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.model.reservation.Reservation;
import com.unimag.unimagleisureinventory.repositories.*;
import com.unimag.unimagleisureinventory.services.CheckOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckOutServiceImpl implements CheckOutService {

    private final StudentRepository studentRepository;
    private final CheckOutRepository checkOutRepository;
    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;
    private final PenaltyRepository penaltyRepository;
    private final PenaltyTypeRepository penaltyTypeRepository;
    private final CheckOutMapper checkOutMapper;

    // RF-13/RF-14 — registrar préstamo
    @Transactional
    public CheckOutResponseDTO create(CreateCheckOutRequestDTO request) {

        if (request.reservationId() == null && request.itemId() == null) {
            throw new RuntimeException("Either reservationId or itemId must be provided");
        }

        Item item;
        Student student;

        if (request.reservationId() != null) {
            // viene de una reserva
            Reservation reservation = reservationRepository.findById(request.reservationId())
                    .orElseThrow(() -> new RuntimeException("Reservation not found"));

            if (reservation.getStatus() != ReservationStatus.ACCEPTED) {
                throw new RuntimeException("Reservation must be accepted before checkout");
            }

            item = reservation.getItem();
            student = reservation.getStudent();

        } else {
            // préstamo directo sin reserva
            item = itemRepository.findById(request.itemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (item.getAvailableQuantity() <= 0) {
                throw new RuntimeException("Item not available");
            }

            student = studentRepository.findById(request.studentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
        }

        CheckOut checkOut = new CheckOut();
        checkOut.setReservation(request.reservationId() != null
                ? reservationRepository.findById(request.reservationId()).get()
                : null);
        checkOut.setCheckOutDate(LocalDateTime.now());
        checkOut.setStudent(student);
        checkOut.setDueDate(calculateDueDate(LocalDateTime.now()));
        checkOut.setStatus(CheckOutStatus.ACTIVE);

        itemRepository.decrementAvailableQuantity(item.getItemId());

        return checkOutMapper.toResponseDTO(checkOutRepository.save(checkOut));
    }

    // RF-16 — registrar devolución
    @Transactional
    public CheckOutResponseDTO checkIn(UUID checkOutId, CheckInRequestDTO request) {
        CheckOut checkOut = checkOutRepository.findById(checkOutId)
                .orElseThrow(() -> new RuntimeException("CheckOut not found"));

        if (checkOut.getStatus() != CheckOutStatus.ACTIVE) {
            throw new RuntimeException("CheckOut is not active");
        }

        checkOut.setCheckInDate(LocalDateTime.now());
        checkOut.setReturnedItemCondition(request.returnedItemCondition());
        checkOut.setStatus(CheckOutStatus.RETURNED);

        // RF-19 — liberar artículo en inventario
        itemRepository.incrementAvailableQuantity(
                checkOut.getReservation().getItem().getItemId());

        // RF-22 — activar sanción si artículo en mal estado
        if (request.returnedItemCondition() == ItemCondition.DAMAGED) {
            activatePenalty(checkOut, "DAMAGED_ITEM");
        }

        return checkOutMapper.toResponseDTO(checkOutRepository.save(checkOut));
    }

    // RF-17 — evaluar estado del artículo devuelto
    @Transactional
    public CheckOutResponseDTO updateCondition(UUID checkOutId, CheckInRequestDTO request) {
        CheckOut checkOut = checkOutRepository.findById(checkOutId)
                .orElseThrow(() -> new RuntimeException("CheckOut not found"));

        checkOut.setReturnedItemCondition(request.returnedItemCondition());

        // RF-22 — activar sanción si se actualiza a mal estado
        if (request.returnedItemCondition() == ItemCondition.DAMAGED) {
            activatePenalty(checkOut, "DAMAGED_ITEM");
        }

        return checkOutMapper.toResponseDTO(checkOutRepository.save(checkOut));
    }

    // RF-27/RF-28 — historial de préstamos
    public List<CheckOutResponseDTO> getByStudent(Long studentId) {
        return checkOutRepository.findByReservation_Student_Id(studentId)
                .stream()
                .map(checkOutMapper::toResponseDTO)
                .toList();
    }

    // RF-21/RF-22 — activar sanción
    private void activatePenalty(CheckOut checkOut, String penaltyTypeName) {
        boolean alreadyPenalized = penaltyRepository.existsByStudent_IdAndStatus(
                checkOut.getReservation().getStudent().getStudentId(),
                PenaltyStatus.ACTIVE);

        if (alreadyPenalized) return;

        Optional<PenaltyType> penaltyTypeOpt = penaltyTypeRepository.findByName(penaltyTypeName);
        PenaltyType penaltyType = penaltyTypeOpt
                .orElseThrow(() -> new RuntimeException("Penalty type not found: " + penaltyTypeName));

        Penalty penalty = new Penalty();
        penalty.setStudent(checkOut.getReservation().getStudent());
        penalty.setCheckOut(checkOut);
        penalty.setPenaltyType(penaltyType);
        penalty.setReason(penaltyTypeName.equals("DAMAGED_ITEM")
                ? "Item returned in damaged condition"
                : "Item not returned on time");
        penalty.setStartDate(LocalDateTime.now());
        penalty.setPenaltyStatus(PenaltyStatus.ACTIVE);

        penaltyRepository.save(penalty);
    }

    private LocalDateTime calculateDueDate(LocalDateTime checkOutDate) {
        LocalTime checkOutTime = checkOutDate.toLocalTime();
        LocalTime morningClose = LocalTime.of(12, 0);
        LocalTime afternoonOpen = LocalTime.of(14, 0);
        LocalTime afternoonClose = LocalTime.of(18, 0);

        if (checkOutTime.isBefore(morningClose)) {
            // prestado en la mañana → devolver a las 12pm
            return checkOutDate.toLocalDate().atTime(morningClose);
        } else if (checkOutTime.isAfter(afternoonOpen) || checkOutTime.equals(afternoonOpen)) {
            // prestado en la tarde → devolver a las 6pm
            return checkOutDate.toLocalDate().atTime(afternoonClose);
        } else {
            // prestado entre 12pm y 2pm — no debería ocurrir
            throw new RuntimeException("Service is closed between 12:00 and 14:00");
        }
    }
}
