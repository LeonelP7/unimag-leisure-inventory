package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.logs.*;
import com.unimag.unimagleisureinventory.mappers.PersonMapper;
import com.unimag.unimagleisureinventory.model.enums.*;
import com.unimag.unimagleisureinventory.repositories.logs.*;
import com.unimag.unimagleisureinventory.services.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final ReservationStatusLogsRepository reservationLogsRepository;
    private final CheckOutStatusLogsRepository checkOutLogsRepository;
    private final PenaltyStatusLogsRepository penaltyLogsRepository;
    private final ItemStatusLogsRepository itemStatusLogsRepository;
    private final ItemConditionLogsRepository itemConditionLogsRepository;
    private final PersonMapper personMapper;

    public List<ReservationStatusLogResponseDTO> getReservationLogs(
            LocalDateTime from, LocalDateTime to, Role role, ReservationStatus newStatus) {
        return reservationLogsRepository.findWithFilters(from, to, role, newStatus)
                .stream()
                .map(l -> new ReservationStatusLogResponseDTO(
                        l.getReservationStatusLogId(),
                        l.getPreviousStatus(),
                        l.getNewStatus(),
                        l.getRecordedAt(),
                        personMapper.toResponseDTO(l.getTriggeredBy())
                )).toList();
    }

    public List<CheckOutStatusLogResponseDTO> getCheckOutLogs(
            LocalDateTime from, LocalDateTime to, Role role, CheckOutStatus newStatus) {
        return checkOutLogsRepository.findWithFilters(from, to, role, newStatus)
                .stream()
                .map(l -> new CheckOutStatusLogResponseDTO(
                        l.getCheckoutStatusLogId(),
                        l.getPreviousStatus(),
                        l.getNewStatus(),
                        l.getRecordedAt(),
                        personMapper.toResponseDTO(l.getTriggeredBy())
                )).toList();
    }

    public List<PenaltyStatusLogResponseDTO> getPenaltyLogs(
            LocalDateTime from, LocalDateTime to, Role role, PenaltyStatus newStatus) {
        return penaltyLogsRepository.findWithFilters(from, to, role, newStatus)
                .stream()
                .map(l -> new PenaltyStatusLogResponseDTO(
                        l.getPenaltyStatusLogsId(),
                        l.getPreviousStatus(),
                        l.getNewStatus(),
                        l.getRecordedAt(),
                        personMapper.toResponseDTO(l.getTriggeredBy())
                )).toList();
    }

    public List<ItemStatusLogResponseDTO> getItemStatusLogs(
            LocalDateTime from, LocalDateTime to, Role role, ItemStatus newStatus) {
        return itemStatusLogsRepository.findWithFilters(from, to, role, newStatus)
                .stream()
                .map(l -> new ItemStatusLogResponseDTO(
                        l.getItemStatusLogId(),
                        l.getPreviousStatus(),
                        l.getNewStatus(),
                        l.getRecordedAt(),
                        personMapper.toResponseDTO(l.getTriggeredBy())
                )).toList();
    }

    public List<ItemConditionLogResponseDTO> getItemConditionLogs(
            LocalDateTime from, LocalDateTime to, Role role, ItemCondition newCondition) {
        return itemConditionLogsRepository.findWithFilters(from, to, role, newCondition)
                .stream()
                .map(l -> new ItemConditionLogResponseDTO(
                        l.getItemConditionLogId(),
                        l.getPreviousCondition(),
                        l.getNewCondition(),
                        l.getNotes(),
                        l.getRecordedAt(),
                        personMapper.toResponseDTO(l.getRegisteredBy())
                )).toList();
    }
}
