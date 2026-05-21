package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.person.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // Buscar por correo del person asociado
    Optional<Student> findByPerson_Email(String email);

    // Verificar si existe un estudiante con ese correo
    boolean existsByPerson_Email(String email);
}
