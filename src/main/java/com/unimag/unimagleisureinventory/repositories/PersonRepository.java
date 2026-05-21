package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.enums.Role;
import com.unimag.unimagleisureinventory.model.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findByEmail(String email);

    // Validar correo único antes de registrar (RF-03)
    boolean existsByEmail(String email);

    // Buscar todos los usuarios por rol (gestión de usuarios RF-01)
    List<Person> findByRole(Role role);
}
