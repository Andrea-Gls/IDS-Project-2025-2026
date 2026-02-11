package com.gasing.hackhub.model;

import jakarta.persistence.*;
import lombok.*;
import com.gasing.hackhub.enums.Role;


@Entity
@Table(name = "staff_assignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- ENUM ROLE ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // --- RELAZIONE CON USER ---
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- RELAZIONE CON HACKATHON ---
    @ManyToOne(optional = false)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;
}
