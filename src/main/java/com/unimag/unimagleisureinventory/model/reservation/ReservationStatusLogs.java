package com.unimag.unimagleisureinventory.model.reservation;

import com.unimag.unimagleisureinventory.model.Person;
import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReservationStatusLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reservationStatusLogId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_id", nullable = false)
    private Person triggeredBy;
    @Enumerated(EnumType.STRING)
    private ReservationStatus previousStatus;
    @Enumerated(EnumType.STRING)
    private ReservationStatus newStatus;
    private LocalDateTime recordedAt;
}
