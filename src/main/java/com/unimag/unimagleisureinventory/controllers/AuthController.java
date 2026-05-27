package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.login.LoginRequestDTO;
import com.unimag.unimagleisureinventory.dtos.login.LoginResponseDTO;
import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.services.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final SecurityUtils securityUtils;

    @PostMapping("/login")
    @PreAuthorize("isAnonymous() or isAuthenticated()")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PersonResponseDTO> getMyId(
            HttpServletRequest request) { // TODO ResponseEntity.ok(userService.getStudentById(securityUtils.getCurrentStudentId(request)));
        return null;

    }
}
