package com.gasing.hackhub.model;

import com.gasing.hackhub.enums.RequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String problema;  // descrizione del problema che il team sta affrontando

    @Column(nullable = false)
    private String callLink;  // link alla videochiamata per il supporto
    
    @Column(nullable = false)
    private RequestStatus status;  // stato della richiesta (OPEN, RESOLVED)

    @Column(nullable = false)
    private Team team;  // il team che ha fatto la richiesta

    @Column(nullable = false)
    private StaffAssignment mentor;  // il mentore assegnato alla richiesta
}
