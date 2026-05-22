package com.unimag.unimagleisureinventory.services;

import com.unimag.unimagleisureinventory.config.security.JwtUtil;
import com.unimag.unimagleisureinventory.dtos.LoginRequestDTO;
import com.unimag.unimagleisureinventory.dtos.LoginResponseDTO;
import com.unimag.unimagleisureinventory.model.person.Person;
import com.unimag.unimagleisureinventory.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

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
