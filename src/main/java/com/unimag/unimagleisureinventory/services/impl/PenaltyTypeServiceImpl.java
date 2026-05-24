package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyTypeResponseDTO;
import com.unimag.unimagleisureinventory.exceptions.BusinessException;
import com.unimag.unimagleisureinventory.exceptions.ResourceNotFoundException;
import com.unimag.unimagleisureinventory.mappers.PenaltyTypeMapper;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import com.unimag.unimagleisureinventory.repositories.PenaltyTypeRepository;
import com.unimag.unimagleisureinventory.services.PenaltyTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PenaltyTypeServiceImpl implements PenaltyTypeService {

    private final PenaltyTypeRepository penaltyTypeRepository;
    private final PenaltyTypeMapper penaltyTypeMapper;

    public PenaltyTypeResponseDTO getById(UUID id) {
        return penaltyTypeMapper.toResponseDTO(
                penaltyTypeRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Penalty type not found"))
        );
    }

    public List<PenaltyTypeResponseDTO> getAll() {
        return penaltyTypeRepository.findAll()
                .stream()
                .map(penaltyTypeMapper::toResponseDTO)
                .toList();
    }

    public PenaltyTypeResponseDTO create(CreatePenaltyTypeRequestDTO request) {
        if (penaltyTypeRepository.existsByName(request.name())) {
            throw new BusinessException("Penalty type already exists");
        }
        return penaltyTypeMapper.toResponseDTO(
                penaltyTypeRepository.save(penaltyTypeMapper.toEntity(request))
        );
    }

    public PenaltyTypeResponseDTO update(UUID id, CreatePenaltyTypeRequestDTO request) {
        PenaltyType penaltyType = penaltyTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Penalty type not found"));

        if (penaltyTypeRepository.existsByName(request.name())) {
            throw new BusinessException("Penalty type name already in use");
        }

        penaltyType.setName(request.name());
        return penaltyTypeMapper.toResponseDTO(penaltyTypeRepository.save(penaltyType));
    }

    public void delete(UUID id) {
        if (!penaltyTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Penalty type not found");
        }
        penaltyTypeRepository.deleteById(id);
    }
}
