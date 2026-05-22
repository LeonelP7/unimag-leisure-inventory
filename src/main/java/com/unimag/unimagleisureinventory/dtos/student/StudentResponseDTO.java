package com.unimag.unimagleisureinventory.dtos.student;

import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;

public record StudentResponseDTO(
        Long studentId,
        PersonResponseDTO person
) {
}
