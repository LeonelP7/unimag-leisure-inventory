package com.unimag.unimagleisureinventory.model.reservation;

import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.model.enums.ReservationStatus;
import com.unimag.unimagleisureinventory.model.item.Item;
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
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime claimDeadline;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
