package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.logs.*;
import com.unimag.unimagleisureinventory.model.enums.*;

import java.time.LocalDateTime;
import java.util.List;

public interface LogService {
    List<ReservationStatusLogResponseDTO> getReservationLogs(
            LocalDateTime from, LocalDateTime to, Role role, ReservationStatus newStatus);
    List<CheckOutStatusLogResponseDTO> getCheckOutLogs(
            LocalDateTime from, LocalDateTime to, Role role, CheckOutStatus newStatus);
    List<PenaltyStatusLogResponseDTO> getPenaltyLogs(
            LocalDateTime from, LocalDateTime to, Role role, PenaltyStatus newStatus);
    List<ItemStatusLogResponseDTO> getItemStatusLogs(
            LocalDateTime from, LocalDateTime to, Role role, ItemStatus newStatus);
    List<ItemConditionLogResponseDTO> getItemConditionLogs(
            LocalDateTime from, LocalDateTime to, Role role, ItemCondition newCondition);
}
