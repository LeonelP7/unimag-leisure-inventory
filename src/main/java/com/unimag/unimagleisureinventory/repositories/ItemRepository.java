package com.unimag.unimagleisureinventory.repositories;

import com.unimag.unimagleisureinventory.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {

    @Query("SELECT i FROM Item i WHERE i.availableQuantity > 0")
    List<Item> findAvailableItems();

    // RF-06 — filtrar por categoría
    List<Item> findByItemType_Id(UUID itemTypeId);

    // RF-06 — filtrar por nombre
    List<Item> findByNameContainingIgnoreCase(String name);

    // RF-06 — filtrar por nombre y categoría a la vez
    List<Item> findByNameContainingIgnoreCaseAndItemType_Id(String name, UUID itemTypeId);

    // RF-14 — actualizar inventario al confirmar entrega
    @Modifying
    @Query("UPDATE Item i SET i.availableQuantity = i.availableQuantity - 1 WHERE i.itemId = :itemId AND i.availableQuantity > 0")
    int decrementAvailableQuantity(UUID itemId);

    // RF-19 — liberar artículo al completar devolución
    @Modifying
    @Query("UPDATE Item i SET i.availableQuantity = i.availableQuantity + 1 WHERE i.itemId = :itemId")
    int incrementAvailableQuantity(UUID itemId);

    List<Item> findByNameContainingIgnoreCaseAndItemType_IdItemType(String name, UUID itemTypeId);

    List<Item> findByItemType_IdItemType(UUID itemTypeId);
}
