package com.unimag.unimagleisureinventory.mappers;

import com.unimag.unimagleisureinventory.dtos.checkout.CheckOutResponseDTO;
import com.unimag.unimagleisureinventory.dtos.checkout.CreateCheckOutRequestDTO;
import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ReservationMapper.class})
public interface CheckOutMapper {
    CheckOutResponseDTO toResponseDTO(CheckOut checkOut);

    @Mapping(target = "checkOutId", ignore = true)
    @Mapping(target = "checkOutDate", ignore = true)
    @Mapping(target = "dueDate", ignore = true)
    @Mapping(target = "checkInDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "returnedItemCondition", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "student", ignore = true)
    CheckOut toEntity(CreateCheckOutRequestDTO request);
}
