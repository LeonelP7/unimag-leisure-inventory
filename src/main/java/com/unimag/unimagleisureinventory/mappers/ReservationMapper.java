package com.unimag.unimagleisureinventory.mappers;

import com.unimag.unimagleisureinventory.dtos.reservation.CreateReservationRequestDTO;
import com.unimag.unimagleisureinventory.dtos.reservation.ReservationResponseDTO;
import com.unimag.unimagleisureinventory.model.reservation.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {StudentMapper.class, ItemMapper.class})
public interface ReservationMapper {

    ReservationResponseDTO toResponseDTO(Reservation reservation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "item", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "claimDeadline", ignore = true)
    @Mapping(target = "status", ignore = true)
    Reservation toEntity(CreateReservationRequestDTO request);
}
