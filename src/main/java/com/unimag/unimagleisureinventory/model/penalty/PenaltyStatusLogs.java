package com.unimag.unimagleisureinventory.model.penalty;

import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.person.Person;
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
public class PenaltyStatusLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID penaltyStatusLogsId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "penalty_id", nullable = false)
    private Penalty penalty;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_id", nullable = false)
    private Person triggeredBy;
    @Enumerated(EnumType.STRING)
    private PenaltyStatus previousStatus;
    @Enumerated(EnumType.STRING)
    private PenaltyStatus newStatus;
    private LocalDateTime recordedAt;
}
