package com.gasing.hackhub.model;

import com.gasing.hackhub.enums.InviteStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status; // PENDING, ACCEPTED, REJECTED

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User receiver; // L'utente invitato

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team; // Il team che invita
}
