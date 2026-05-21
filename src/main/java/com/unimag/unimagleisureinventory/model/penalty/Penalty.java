package com.unimag.unimagleisureinventory.model.penalty;

import com.unimag.unimagleisureinventory.model.enums.PenaltyStatus;
import com.unimag.unimagleisureinventory.model.person.Student;
import com.unimag.unimagleisureinventory.model.checkout.CheckOut;
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
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID penaltyId;
    private String reason;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @OneToOne
    @JoinColumn(name = "checkout_id", nullable = true)
    private CheckOut checkOut;
    @OneToOne
    @JoinColumn(name = "student_id", nullable = true)
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="penalty_type_id", nullable=false)
    private PenaltyType penaltyType;
    @Enumerated(EnumType.STRING)
    private PenaltyStatus penaltyStatus;
}
