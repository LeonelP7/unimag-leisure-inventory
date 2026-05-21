package com.unimag.unimagleisureinventory.model.person;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
    @Id
    @Column(nullable = false, unique = true)
    private Long studentId;
}
