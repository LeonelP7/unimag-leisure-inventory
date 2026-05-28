package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.checkout.CheckInRequestDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CheckOutResponseDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CreateCheckOutRequestDTO;
import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CheckOutService {
    CheckOutResponseDTO create(CreateCheckOutRequestDTO request);
    CheckOutResponseDTO checkIn(UUID checkOutId, CheckInRequestDTO request);
    // CheckOutResponseDTO updateCondition(UUID checkOutId, CheckInRequestDTO request);
    List<CheckOutResponseDTO> getByStudent(Long studentId);
    CheckOutResponseDTO getById(UUID id);
    CheckOutResponseDTO getActiveByStudent(Long studentId);
    List<CheckOutResponseDTO> getAll(CheckOutStatus status, LocalDateTime from);
}
