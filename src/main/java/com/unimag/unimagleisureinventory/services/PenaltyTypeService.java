package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyTypeResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PenaltyTypeService {
    List<PenaltyTypeResponseDTO> getAll();
    PenaltyTypeResponseDTO create(CreatePenaltyTypeRequestDTO request);
    PenaltyTypeResponseDTO update(UUID id, CreatePenaltyTypeRequestDTO request);
    void delete(UUID id);
}
