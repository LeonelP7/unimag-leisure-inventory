package com.unimag.unimagleisureinventory.config.security;

import com.unimag.unimagleisureinventory.model.person.Person;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final JwtUtil jwtUtil;

    public Long getCurrentStudentId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        return jwtUtil.extractStudentId(token);
    }

    public Person getCurrentPerson() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null; // tarea programada, no hay usuario
        }
        return (Person) auth.getPrincipal();
    }
}
