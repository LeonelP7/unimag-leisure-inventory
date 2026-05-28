package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyResponseDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.ResolvePenaltyRequestDTO;

import java.util.List;
import java.util.UUID;

public interface PenaltyService {
    PenaltyResponseDTO create(CreatePenaltyRequestDTO request);
    PenaltyResponseDTO resolve(UUID penaltyId, ResolvePenaltyRequestDTO request);
    List<PenaltyResponseDTO> getByStudent(Long studentId);
    boolean hasActivePenalty(Long studentId);
    PenaltyResponseDTO getById(UUID id);
    List<PenaltyResponseDTO> getAll();
}
