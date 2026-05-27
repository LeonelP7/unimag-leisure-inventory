package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.person.CreatePersonRequestDTO;
import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.dtos.student.CreateStudentRequestDTO;
import com.unimag.unimagleisureinventory.dtos.student.StudentResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PersonResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/person")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonResponseDTO> createPerson(
            @Valid @RequestBody CreatePersonRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createPerson(request));
    }

    @PostMapping("/student")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponseDTO> createStudent(
            @Valid @RequestBody CreateStudentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createStudent(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonResponseDTO>> getByRole(
            @RequestParam Role role) {
        return ResponseEntity.ok(userService.getByRole(role));
    }

    @PutMapping("/{personId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonResponseDTO> updatePerson(
            @PathVariable UUID personId,
            @Valid @RequestBody CreatePersonRequestDTO request) {
        return ResponseEntity.ok(userService.updatePerson(personId, request));
    }

    @DeleteMapping("/{personId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID personId) {
        userService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }
}
