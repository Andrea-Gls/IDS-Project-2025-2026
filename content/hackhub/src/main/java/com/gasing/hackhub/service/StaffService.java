package com.gasing.hackhub.service;

import com.gasing.hackhub.dto.staff.AddStaffRequest;
import com.gasing.hackhub.dto.staff.ReportViolationRequest;
import com.gasing.hackhub.enums.Role;
import com.gasing.hackhub.model.*;
import com.gasing.hackhub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffService {

    @Autowired private StaffAssignmentRepository staffAssignmentRepository;
    @Autowired private HackathonRepository hackathonRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private ViolationRepository violationRepository;

    // --- 1. AGGIUNGI STAFF (Spostato qui come da tua lista) ---
    @Transactional
    public void addStaffMember(AddStaffRequest req) {
        // Verifica permessi Organizzatore
        StaffAssignment organizer = staffAssignmentRepository.findByHackathonIdAndUserId(req.getHackathonId(), req.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Non sei nello staff di questo evento"));

        if (organizer.getRole() != Role.ORGANIZER) {
            throw new RuntimeException("Solo l'Organizzatore può aggiungere staff!");
        }

        // Recupero Entità
        Hackathon hackathon = hackathonRepository.findById(req.getHackathonId()).orElseThrow();
        User newStaff = userRepository.findByEmail(req.getEmailUtente()) // Usiamo la mail come da DTO
                .orElseThrow(() -> new RuntimeException("Utente non trovato con email: " + req.getEmailUtente()));

        // Controllo se è già staff
        if (staffAssignmentRepository.existsByHackathonIdAndUserId(hackathon.getId(), newStaff.getId())) {
            throw new RuntimeException("Utente già nello staff!");
        }

        // Salvo
        StaffAssignment assignment = new StaffAssignment();
        assignment.setUser(newStaff);
        assignment.setHackathon(hackathon);
        assignment.setRole(req.getRuolo()); // Assumiamo che AddStaffRequest abbia un campo Role
        staffAssignmentRepository.save(assignment);
    }

    // --- 2. SEGNALA VIOLAZIONE (Mentore -> Report) ---
    @Transactional
    public ViolationReport reportTeam(ReportViolationRequest req) {
        // Verifico che chi segnala sia STAFF in quell'hackathon
        StaffAssignment reporter = staffAssignmentRepository.findByHackathonIdAndUserId(req.getHackathonId(), req.getReporterId())
                .orElseThrow(() -> new RuntimeException("Non sei staff in questo evento!"));

        // Il testo dice "Il Mentore... può segnalare".
        if (reporter.getRole() != Role.MENTOR) {
            // Opzionale: se vuoi essere rigido col testo, scommenta:
            // throw new RuntimeException("Solo i Mentori possono fare segnalazioni!");
        }

        Team team = teamRepository.findById(req.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team non trovato"));

        ViolationReport report = new ViolationReport();
        report.setReporter(reporter);
        report.setReportedTeam(team);
        report.setMotivo(req.getMotivo());

        return violationRepository.save(report);
    }

    // --- 3. (OPZIONALE) SQUALIFICA TEAM (Organizzatore -> Azione) ---
    // Questa parte la chiamerà l'Organizzatore leggendo i report
    @Transactional
    public void squalificaTeam(Long organizerId, Long hackathonId, Long teamId) {
        // Verifica che sia Organizzatore...
        StaffAssignment staff = staffAssignmentRepository.findByHackathonIdAndUserId(hackathonId, organizerId)
                .orElseThrow(() -> new RuntimeException("Staff non trovato"));

        if(staff.getRole() != Role.ORGANIZER) throw new RuntimeException("Solo l'admin squalifica!");

        Team team = teamRepository.findById(teamId).orElseThrow();
        team.setDisqualified(true); // Imposta il flag su Team
        teamRepository.save(team);
    }
}