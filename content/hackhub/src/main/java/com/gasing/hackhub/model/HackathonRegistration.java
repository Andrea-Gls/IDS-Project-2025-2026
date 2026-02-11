package com.gasing.hackhub.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "hackathon_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HackathonRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataRegistrazione;

    @Column(nullable = false)
    private boolean winner;

    // Molte registrazioni possono appartenere allo stesso Team
    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // Molte registrazioni possono appartenere allo stesso Hackathon
    @ManyToOne(optional = false)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;
}

