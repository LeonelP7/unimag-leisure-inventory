package com.unimag.unimagleisureinventory.mappers;

import com.unimag.unimagleisureinventory.dtos.CreatePenaltyRequestDTO;
import com.unimag.unimagleisureinventory.dtos.PenaltyResponseDTO;
import com.unimag.unimagleisureinventory.model.penalty.Penalty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentMapper.class})
public interface PenaltyMapper {

    @Mapping(source = "penaltyType.idPenaltyType", target = "penaltyType.idPenaltyType")
    @Mapping(source = "penaltyType.name", target = "penaltyType.name")
    PenaltyResponseDTO toResponseDTO(Penalty penalty);

    @Mapping(target = "penaltyId", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "penaltyStatus", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "checkOut", ignore = true)
    @Mapping(target = "penaltyType", ignore = true)
    Penalty toEntity(CreatePenaltyRequestDTO request);
}
