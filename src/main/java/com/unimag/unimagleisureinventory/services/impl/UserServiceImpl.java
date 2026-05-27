package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.dtos.person.CreatePersonRequestDTO;
import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.dtos.student.CreateStudentRequestDTO;
import com.unimag.unimagleisureinventory.dtos.student.StudentResponseDTO;
import com.unimag.unimagleisureinventory.exceptions.BusinessException;
import com.unimag.unimagleisureinventory.exceptions.ResourceNotFoundException;
import com.unimag.unimagleisureinventory.mappers.PersonMapper;
import com.unimag.unimagleisureinventory.mappers.StudentMapper;
import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.model.person.Person;
import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.repositories.PersonRepository;
import com.unimag.unimagleisureinventory.repositories.StudentRepository;

import com.unimag.unimagleisureinventory.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PersonRepository personRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final PersonMapper personMapper;
    private final StudentMapper studentMapper;

    public PersonResponseDTO getById(UUID id) {
        return personMapper.toResponseDTO(
                personRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Person not found"))
        );
    }

    public PersonResponseDTO createPerson(CreatePersonRequestDTO request) {
        if (personRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use");
        }

        Person person = personMapper.toEntity(request);
        person.setPassword(passwordEncoder.encode(request.password()));
        person.setRole(request.role());

        return personMapper.toResponseDTO(personRepository.save(person));
    }

    @Transactional
    public StudentResponseDTO createStudent(CreateStudentRequestDTO request) {
        if (personRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use");
        }
        if (studentRepository.existsById(request.studentId())) {
            throw new BusinessException("Student ID already registered");
        }

        Person person = new Person();
        person.setFirstName(request.firstName());
        person.setLastName(request.lastName());
        person.setEmail(request.email());
        person.setPassword(passwordEncoder.encode(request.password()));
        person.setRole(Role.STUDENT);

        Student student = new Student();
        student.setStudentId(request.studentId());
        student.setPerson(personRepository.save(person));

        return studentMapper.toResponseDTO(studentRepository.save(student));
    }

    public List<PersonResponseDTO> getByRole(Role role) {
        return personRepository.findByRole(role)
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();
    }

    public PersonResponseDTO updatePerson(UUID personId, CreatePersonRequestDTO request) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));

        person.setFirstName(request.firstName());
        person.setLastName(request.lastName());
        person.setEmail(request.email());
        person.setPassword(passwordEncoder.encode(request.password()));

        return personMapper.toResponseDTO(personRepository.save(person));
    }

    public void deletePerson(UUID personId) {
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("Person not found");
        }
        personRepository.deleteById(personId);
    }

    @Override
    public List<StudentResponseDTO> searchStudents(String query) {
        return studentRepository.findAll().stream()
                .filter(s ->
                        s.getPerson().getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                                s.getPerson().getLastName().toLowerCase().contains(query.toLowerCase()) ||
                                s.getStudentId().toString().contains(query)
                )
                .map(studentMapper::toResponseDTO)
                .toList();
    }

    @Override
    public StudentResponseDTO getStudentById(Long StudentId) {
        return studentRepository.findById(StudentId).map(studentMapper::toResponseDTO).orElse(null);
    }
}
