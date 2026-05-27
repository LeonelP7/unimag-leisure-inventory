package com.unimag.unimagleisureinventory.controllers;

import com.unimag.unimagleisureinventory.config.security.SecurityUtils;
import com.unimag.unimagleisureinventory.dtos.student.StudentResponseDTO;
import com.unimag.unimagleisureinventory.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final UserService userService;

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'INVENTORY_CLERK')")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(
            @RequestParam String q) {
        return ResponseEntity.ok(userService.searchStudents(q));
    }
}
