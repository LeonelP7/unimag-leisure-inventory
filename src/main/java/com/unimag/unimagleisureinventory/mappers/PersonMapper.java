package com.unimag.unimagleisureinventory.mappers;

import com.unimag.unimagleisureinventory.dtos.person.CreatePersonRequestDTO;
import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.model.person.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonResponseDTO toResponseDTO(Person person);

    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "role", constant = "STUDENT")
    @Mapping(target = "password", ignore = true) // se encripta en el service
    Person toEntity(CreatePersonRequestDTO request);
}
