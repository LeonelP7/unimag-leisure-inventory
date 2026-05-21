package com.unimag.unimagleisureinventory.model.checkout;

import com.unimag.unimagleisureinventory.model.enums.CheckOutStatus;
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
public class CheckOutStatusLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID checkoutStatusLogId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id", nullable = false)
    private CheckOut checkout;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clerk_id", nullable = false)
    private Person triggeredBy;
    @Enumerated(EnumType.STRING)
    private CheckOutStatus previousStatus;
    @Enumerated(EnumType.STRING)
    private CheckOutStatus newStatus;
    private LocalDateTime recordedAt;

}
