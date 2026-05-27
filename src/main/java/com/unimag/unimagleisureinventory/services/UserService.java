package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.dtos.person.CreatePersonRequestDTO;
import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.dtos.student.CreateStudentRequestDTO;
import com.unimag.unimagleisureinventory.dtos.student.StudentResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.Role;

import java.util.List;
import java.util.UUID;

public interface UserService {
    PersonResponseDTO createPerson(CreatePersonRequestDTO request);
    StudentResponseDTO createStudent(CreateStudentRequestDTO request);
    List<PersonResponseDTO> getByRole(Role role);
    PersonResponseDTO updatePerson(UUID personId, CreatePersonRequestDTO request);
    void deletePerson(UUID personId);
    PersonResponseDTO getById(UUID id);
    List<StudentResponseDTO> searchStudents(String query);
    StudentResponseDTO getStudentById(Long StudentId);
}
