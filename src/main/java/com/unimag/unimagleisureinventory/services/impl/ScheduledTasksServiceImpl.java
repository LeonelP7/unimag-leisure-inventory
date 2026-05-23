package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.repositories.CheckOutRepository;
import com.unimag.unimagleisureinventory.repositories.PenaltyRepository;
import com.unimag.unimagleisureinventory.repositories.PenaltyTypeRepository;
import com.unimag.unimagleisureinventory.services.ScheduledTasksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledTasksServiceImpl implements ScheduledTasksService {

    private final CheckOutRepository checkOutRepository;
    private final PenaltyRepository penaltyRepository;
    private final PenaltyTypeRepository penaltyTypeRepository;

    private static final int GRACE_PERIOD_MINUTES = 15;

    // Se ejecuta cada 5 minutos
    @Scheduled(fixedRate = 300000)
    public void checkOverdueCheckOuts() {
        log.info("Running overdue checkout check at {}", LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime graceCutoff = now.minusMinutes(GRACE_PERIOD_MINUTES);

        // Buscar préstamos activos cuyo dueDate + gracia ya pasó
        List<CheckOut> overdueCheckOuts = checkOutRepository
                .findByStatusAndDueDateBefore(CheckOutStatus.ACTIVE, graceCutoff);

        for (CheckOut checkOut : overdueCheckOuts) {
            processOverdue(checkOut);
        }

        log.info("Overdue check finished — {} checkouts processed", overdueCheckOuts.size());
    }

    private void processOverdue(CheckOut checkOut) {
        Student student = checkOut.getReservation() != null
                ? checkOut.getReservation().getStudent()
                : null;

        if (student == null) {
            log.warn("CheckOut {} has no student associated", checkOut.getCheckOutId());
            return;
        }

        // Verificar que no tenga ya una sanción activa por este checkout
        boolean alreadyPenalized = penaltyRepository.existsByStudent_IdAndStatus(
                student.getStudentId(), PenaltyStatus.ACTIVE);

        if (alreadyPenalized) {
            log.info("Student {} already has active penalty, skipping",
                    student.getStudentId());
            return;
        }

        // RF-20 — alerta en log (se puede migrar a email después)
        log.warn("ALERT — CheckOut {} overdue. Student: {} {}. DueDate was: {}",
                checkOut.getCheckOutId(),
                student.getPerson().getFirstName(),
                student.getPerson().getLastName(),
                checkOut.getDueDate());

        // RF-21 — activar sanción automática
        PenaltyType penaltyType = penaltyTypeRepository.findByName("OVERDUE")
                .orElseGet(() -> {
                    // Si no existe, lo crea automáticamente
                    PenaltyType newType = new PenaltyType();
                    newType.setName("OVERDUE");
                    return penaltyTypeRepository.save(newType);
                });

        Penalty penalty = new Penalty();
        penalty.setStudent(student);
        penalty.setCheckOut(checkOut);
        penalty.setPenaltyType(penaltyType);
        penalty.setReason("Item not returned on time. Due: " + checkOut.getDueDate());
        penalty.setStartDate(LocalDateTime.now());
        penalty.setPenaltyStatus(PenaltyStatus.ACTIVE);

        penaltyRepository.save(penalty);

        // Marcar checkout como expirado
        checkOut.setStatus(CheckOutStatus.EXPIRED);
        checkOutRepository.save(checkOut);

        log.info("Penalty activated for student {}", student.getStudentId());
    }
}
