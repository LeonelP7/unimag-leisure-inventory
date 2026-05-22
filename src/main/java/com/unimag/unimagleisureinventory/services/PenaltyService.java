package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.CreatePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.dtos.PenaltyResponseDTO;
import com.unimag.unimagleisureinventory.dtos.ResolvePenaltyRequestDTO;

import java.util.List;
import java.util.UUID;

public interface PenaltyService {
    PenaltyResponseDTO create(CreatePenaltyRequestDTO request);
    PenaltyResponseDTO resolve(UUID penaltyId, ResolvePenaltyRequestDTO request);
    List<PenaltyResponseDTO> getByStudent(Long studentId);
    boolean hasActivePenalty(Long studentId);
}
