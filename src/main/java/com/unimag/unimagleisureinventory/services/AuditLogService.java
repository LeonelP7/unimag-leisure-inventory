package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import com.unimag.unimagleisureinventory.model.enums.*;
import com.unimag.unimagleisureinventory.model.item.Item;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import com.unimag.unimagleisureinventory.model.reservation.Reservation;

public interface AuditLogService {
    void logReservationStatus(Reservation reservation, ReservationStatus previous, ReservationStatus next);
    void logCheckOutStatus(CheckOut checkOut, CheckOutStatus previous, CheckOutStatus next);
    void logPenaltyStatus(Penalty penalty, PenaltyStatus previous, PenaltyStatus next);
    void logItemStatus(Item item, ItemStatus previous, ItemStatus next);
    void logItemCondition(Item item, ItemCondition previous, ItemCondition next, String notes);
}
