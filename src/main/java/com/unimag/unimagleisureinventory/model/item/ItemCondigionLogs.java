package com.unimag.unimagleisureinventory.model.item;

import com.unimag.unimagleisureinventory.model.person.Person;
import com.unimag.unimagleisureinventory.model.enums.ItemCondition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ItemCondigionLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID itemConditionLogId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_id", nullable = false)
    private Person registeredBy;
    @Enumerated(EnumType.STRING)
    private ItemCondition previousCondition;
    @Enumerated(EnumType.STRING)
    private ItemCondition newCondition;
    private LocalDateTime recordedAt;
    private String notes;
}
