package com.unimag.unimagleisureinventory.mappers;

import com.unimag.unimagleisureinventory.dtos.penalty.CreatePenaltyTypeRequestDTO;
import com.unimag.unimagleisureinventory.dtos.penalty.PenaltyTypeResponseDTO;
import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PenaltyTypeMapper {

    PenaltyTypeResponseDTO toResponseDTO(PenaltyType penaltyType);

    @Mapping(target = "penaltyTypeId", ignore = true)
    PenaltyType toEntity(CreatePenaltyTypeRequestDTO request);
}
