package com.gasing.hackhub.service;

import com.gasing.hackhub.dto.hackathon.HackathonDTO;
import com.gasing.hackhub.enums.HackathonStatus;
import com.gasing.hackhub.enums.Role;
import com.gasing.hackhub.model.Hackathon;
import com.gasing.hackhub.model.StaffAssignment;
import com.gasing.hackhub.model.User;
import com.gasing.hackhub.repository.HackathonRepository;
import com.gasing.hackhub.repository.StaffAssignmentRepository;
import com.gasing.hackhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HackathonService {

    @Autowired
    private HackathonRepository hackathonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StaffAssignmentRepository staffAssignmentRepository;

    // CREAZIONE HACKATHON

    @Transactional
    public Hackathon createHackathon(HackathonDTO request) {

        // VALIDAZIONI PRELIMINARI
        if (hackathonRepository.existsByNome(request.getNome())) {
            throw new RuntimeException("Esiste già un hackathon con questo nome!");
        }

        // Controllo che ci sia almeno un mentore (come da specifiche "uno o più")
        if (request.getMentorIds() == null || request.getMentorIds().isEmpty()) {
            throw new RuntimeException("Devi assegnare almeno un Mentore!");
        }

        // RECUPERO GLI UTENTI (Staff)

        // L'Organizzatore
        User organizerUser = userRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizzatore non trovato"));

        // Il Giudice
        User judgeUser = userRepository.findById(request.getJudgeId())
                .orElseThrow(() -> new RuntimeException("Giudice non trovato"));

        // CREO E SALVO L'HACKATHON
        Hackathon hackathon = new Hackathon();
        hackathon.setNome(request.getNome());
        hackathon.setRegolamento(request.getRegolamento());
        hackathon.setLuogo(request.getLuogo());
        hackathon.setPremio(request.getPremio());
        hackathon.setDimensioneMassimaTeam(request.getDimensioneMassimaTeam());

        hackathon.setDataInizio(request.getDataInizio());
        hackathon.setDataFine(request.getDataFine());
        hackathon.setScadenzaIscrizione(request.getScadenzaIscrizione());

        hackathon.setStato(HackathonStatus.REGISTRATION_OPEN);

        // Salvo per ottenere l'ID
        hackathon = hackathonRepository.save(hackathon);

        // ASSEGNAZIONE RUOLI (Salvo nella tabella StaffAssignment)

        // Assegno l'Organizzatore
        assignRole(organizerUser, hackathon, Role.ORGANIZER);

        // Assegno il Giudice
        assignRole(judgeUser, hackathon, Role.JUDGE);

        // Assegno i Mentori (Ciclo sulla lista degli ID)
        for (Long mentorId : request.getMentorIds()) {
            User mentorUser = userRepository.findById(mentorId)
                    .orElseThrow(() -> new RuntimeException("Mentore con ID " + mentorId + " non trovato"));

            assignRole(mentorUser, hackathon, Role.MENTOR);
        }

        return hackathon;
    }

    // Metodo privato di utilità per non ripetere codice 3 volte
    private void assignRole(User user, Hackathon hackathon, Role role) {
        // Controllo se è già staff (opzionale ma sicuro)
        if (staffAssignmentRepository.existsByHackathonIdAndUserId(hackathon.getId(), user.getId())) {
            throw new RuntimeException("L'utente " + user.getEmail() + " è già staff in questo evento!");
        }

        StaffAssignment assignment = new StaffAssignment();
        assignment.setUser(user);
        assignment.setHackathon(hackathon);
        assignment.setRole(role);
        staffAssignmentRepository.save(assignment);

        //Aggiorno la lista dell'oggetto in memoria
        // Così se guardi dentro 'hackathon' trovi subito lo staff senza dover ricaricare dal DB
        hackathon.getStaff().add(assignment);
    }

}
