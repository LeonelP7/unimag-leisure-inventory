package com.unimag.unimagleisureinventory.model.item;

import com.unimag.unimagleisureinventory.model.Person;
import com.unimag.unimagleisureinventory.model.enums.ItemStatus;
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
public class ItemStatusLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID itemStatusLogId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_id", nullable = false)
    private Person triggeredBy;
    @Enumerated(EnumType.STRING)
    private ItemStatus previousStatus;
    @Enumerated(EnumType.STRING)
    private ItemStatus newStatus;
    private LocalDateTime recordedAt;
}
