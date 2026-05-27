package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.login.LoginRequestDTO;
import com.unimag.unimagleisureinventory.dtos.login.LoginResponseDTO;
import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.model.person.Person;
import com.unimag.unimagleisureinventory.services.UserService;
import com.unimag.unimagleisureinventory.services.impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final AuthServiceImpl authService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @PostMapping("/login")
    @PreAuthorize("isAnonymous() or isAuthenticated()")
    @Operation(summary = "Login", description = "Authenticate user credentials and return a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        Person person = securityUtils.getCurrentPerson();

        if (person.getRole() == Role.STUDENT) {
            Long studentId = securityUtils.getCurrentStudentId(request);
            return ResponseEntity.ok(userService.getStudentById(studentId));
        }

        return ResponseEntity.ok(userService.getById(person.getPersonId()));
    }
}
