package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.checkout.CheckOutStatusLogs;
import com.unimag.unimagleisureinventory.model.enums.*;
import com.unimag.unimagleisureinventory.model.item.Item;
import com.unimag.unimagleisureinventory.model.item.ItemCondigionLogs;
import com.unimag.unimagleisureinventory.model.item.ItemStatusLogs;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyStatusLogs;
import com.unimag.unimagleisureinventory.model.reservation.Reservation;
import com.unimag.unimagleisureinventory.model.reservation.ReservationStatusLogs;
import com.unimag.unimagleisureinventory.repositories.logs.*;
import com.unimag.unimagleisureinventory.services.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final ReservationStatusLogsRepository reservationStatusLogsRepository;
    private final CheckOutStatusLogsRepository checkOutStatusLogsRepository;
    private final PenaltyStatusLogsRepository penaltyStatusLogsRepository;
    private final ItemStatusLogsRepository itemStatusLogsRepository;
    private final ItemConditionLogsRepository itemConditionLogsRepository;
    private final SecurityUtils securityUtils;

    public void logReservationStatus(Reservation reservation,
                                     ReservationStatus previous,
                                     ReservationStatus next) {
        ReservationStatusLogs log = new ReservationStatusLogs();
        log.setReservation(reservation);
        log.setPreviousStatus(previous);
        log.setNewStatus(next);
        log.setTriggeredBy(securityUtils.getCurrentPerson());
        log.setRecordedAt(LocalDateTime.now());
        reservationStatusLogsRepository.save(log);
    }

    public void logCheckOutStatus(CheckOut checkOut,
                                  CheckOutStatus previous,
                                  CheckOutStatus next) {
        CheckOutStatusLogs log = new CheckOutStatusLogs();
        log.setCheckout(checkOut);
        log.setPreviousStatus(previous);
        log.setNewStatus(next);
        log.setTriggeredBy(securityUtils.getCurrentPerson());
        log.setRecordedAt(LocalDateTime.now());
        checkOutStatusLogsRepository.save(log);
    }

    public void logPenaltyStatus(Penalty penalty,
                                 PenaltyStatus previous,
                                 PenaltyStatus next) {
        PenaltyStatusLogs log = new PenaltyStatusLogs();
        log.setPenalty(penalty);
        log.setPreviousStatus(previous);
        log.setNewStatus(next);
        log.setTriggeredBy(securityUtils.getCurrentPerson());
        log.setRecordedAt(LocalDateTime.now());
        penaltyStatusLogsRepository.save(log);
    }

    public void logItemStatus(Item item,
                              ItemStatus previous,
                              ItemStatus next) {
        ItemStatusLogs log = new ItemStatusLogs();
        log.setItem(item);
        log.setPreviousStatus(previous);
        log.setNewStatus(next);
        log.setTriggeredBy(securityUtils.getCurrentPerson());
        log.setRecordedAt(LocalDateTime.now());
        itemStatusLogsRepository.save(log);
    }

    public void logItemCondition(Item item,
                                 ItemCondition previous,
                                 ItemCondition next,
                                 String notes) {
        ItemCondigionLogs log = new ItemCondigionLogs();
        log.setItem(item);
        log.setPreviousCondition(previous);
        log.setNewCondition(next);
        log.setRegisteredBy(securityUtils.getCurrentPerson());
        log.setRecordedAt(LocalDateTime.now());
        log.setNotes(notes);
        itemConditionLogsRepository.save(log);
    }
}
