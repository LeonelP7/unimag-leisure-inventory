package com.unimag.unimagleisureinventory.services.impl;

import com.unimag.unimagleisureinventory.config.security.JwtUtil;
import com.unimag.unimagleisureinventory.dtos.login.LoginRequestDTO;
import com.unimag.unimagleisureinventory.dtos.login.LoginResponseDTO;
import com.unimag.unimagleisureinventory.model.person.Person;
import com.unimag.unimagleisureinventory.repositories.PersonRepository;
import com.unimag.unimagleisureinventory.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO login(LoginRequestDTO request) {

        Person person = personRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), person.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(person);

        return new LoginResponseDTO(token, person.getRole().name(), person.getPersonId());
    }
}
