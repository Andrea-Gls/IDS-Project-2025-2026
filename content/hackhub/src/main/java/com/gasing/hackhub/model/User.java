package com.gasing.hackhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "users") // "user" è parola riservata in SQL, meglio usare plurale
@Data // Lombok genera getter, setter, toString, equals, hashcode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash; // Nel diagramma è passwordHash

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    // Relazione con StaffAssignment (da scommentare quando implementerai l'area Evento&Staff)
    // @OneToMany(mappedBy = "user")
    // private List<StaffAssignment> assignments;
}