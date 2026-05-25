package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.penalty.PenaltyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PenaltyTypeRepository extends JpaRepository<PenaltyType, UUID> {
    Optional<PenaltyType> findByName(String name); // debe retornar Optional
    boolean existsByName(String name);
}
