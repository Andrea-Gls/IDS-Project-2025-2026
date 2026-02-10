package com.gasing.hackhub.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@Table(name = "Utente") // user non si può usare in SQL per nominare una tabella
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id si autoincrementa nel database
    private long id;

    @Column(nullable = false) // non accetta valori vuoti
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false, unique=true) // unique fa si che non si possa usare l'email per due utenti
    private String email;

    @Column(nullable = false)
    private String passwordHash; // la password non deve esser emostrata in chiaro

    @OneToMany(mappedBy = "user") // serve per il collegamento di un utente al suo ruolo staff che può avere in vari hackathon
    private List<StaffAssignment> assignments;

}