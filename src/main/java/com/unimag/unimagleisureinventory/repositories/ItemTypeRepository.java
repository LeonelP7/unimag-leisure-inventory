package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.item.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemTypeRepository extends JpaRepository<ItemType, UUID> {
    Optional<ItemType> findByName(String name);
    boolean existsByName(String name);
}
