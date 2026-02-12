package com.gasing.hackhub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gasing.hackhub.enums.HackathonStatus;


@Entity
@Table(name = "hackathon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false, length = 1000)
    private String descrizione;

    @Column(nullable = false)
    private LocalDate dataInizio;

    @Column(nullable = false)
    private LocalDate dataFine;

    @Column(nullable = false)
    private LocalDate scadenzaIscrizione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HackathonStatus stato;

    // Cascade ALL: Se cancello l'evento, cancello anche l'assegnazione dei giudici.
    @OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Protegge i log dal loop infinito
    @JsonIgnore       // Protegge il JSON
    private List<StaffAssignment> staff = new ArrayList<>();

    // Cascade ALL: Se cancello l'evento, cancello tutte le iscrizioni.
    @OneToMany(mappedBy = "hackathon", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Protegge i log dal loop infinito
    @JsonIgnore       // Protegge il JSON
    private List<HackathonRegistration> registrations = new ArrayList<>();
}
