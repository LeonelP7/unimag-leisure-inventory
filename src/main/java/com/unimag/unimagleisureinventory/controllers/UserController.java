package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.dtos.person.CreatePersonRequestDTO;
import com.unimag.unimagleisureinventory.dtos.person.PersonResponseDTO;
import com.unimag.unimagleisureinventory.dtos.student.CreateStudentRequestDTO;
import com.unimag.unimagleisureinventory.dtos.student.StudentResponseDTO;
import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Admin endpoints for managing users and students")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Returns a single user (person) by their UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<PersonResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/person")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create person", description = "Creates a new non-student user (admin or inventory clerk)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<PersonResponseDTO> createPerson(
            @Valid @RequestBody CreatePersonRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createPerson(request));
    }

    @PostMapping("/student")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create student", description = "Creates a new student user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    public ResponseEntity<StudentResponseDTO> createStudent(
            @Valid @RequestBody CreateStudentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createStudent(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List users by role", description = "Returns all users filtered by the specified role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    public ResponseEntity<List<PersonResponseDTO>> getByRole(
            @RequestParam Role role) {
        return ResponseEntity.ok(userService.getByRole(role));
    }

    @PutMapping("/{personId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update person", description = "Updates an existing user's information by their UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<PersonResponseDTO> updatePerson(
            @PathVariable UUID personId,
            @Valid @RequestBody CreatePersonRequestDTO request) {
        return ResponseEntity.ok(userService.updatePerson(personId, request));
    }

    @DeleteMapping("/{personId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete person", description = "Deletes a user by their UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<Void> deletePerson(@PathVariable UUID personId) {
        userService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }
}
