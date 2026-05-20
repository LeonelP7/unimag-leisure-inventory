package com.unimag.unimagleisureinventory.model;

import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
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
public class CheckOut {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID chekOutId;
    private LocalDateTime checkOutDate;
    private LocalDateTime dueDate;
    private LocalDateTime checkInDate;
    @Enumerated(EnumType.STRING)
    private CheckOutStatus status;
    @Enumerated(EnumType.STRING)
    private ItemCondition returnedItemCondition;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = true)
    private Reservation reservation;
}
