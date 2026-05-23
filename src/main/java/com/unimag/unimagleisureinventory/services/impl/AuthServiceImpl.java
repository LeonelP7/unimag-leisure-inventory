package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.config.security.JwtUtil;
import com.unimag.unimagleisureinventory.dtos.login.LoginRequestDTO;
import com.unimag.unimagleisureinventory.dtos.login.LoginResponseDTO;
import com.unimag.unimagleisureinventory.exceptions.BusinessException;
import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.model.person.Person;
import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.repositories.PersonRepository;
import com.unimag.unimagleisureinventory.repositories.StudentRepository;
import com.unimag.unimagleisureinventory.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final StudentRepository studentRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginRequestDTO request) {

        Person person = personRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), person.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }

        Long studentId = null;
        if (person.getRole() == Role.STUDENT) {
            studentId = studentRepository.findByPerson_Email(person.getEmail())
                    .map(Student::getStudentId)
                    .orElse(null);
        }

        String token = jwtUtil.generateToken(person, studentId);
        return new LoginResponseDTO(token, person.getRole().name(), person.getPersonId());
    }
}
