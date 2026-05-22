package com.unimag.unimagleisureinventory.mappers;

import com.unimag.unimagleisureinventory.dtos.student.StudentResponseDTO;
import com.unimag.unimagleisureinventory.model.person.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PersonMapper.class)
public interface StudentMapper {

    @Mapping(source = "person", target = "person")
    StudentResponseDTO toResponseDTO(Student student);
}
