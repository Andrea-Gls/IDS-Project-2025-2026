package com.gasing.hackhub.controller;

import com.gasing.hackhub.dto.team.request.CreateTeamRequest;
import com.gasing.hackhub.dto.team.request.InviteMemberRequest;
import com.gasing.hackhub.dto.team.response.InviteMemberResponse;
import com.gasing.hackhub.model.Team;
import com.gasing.hackhub.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Rest controller dice a spring che questa classe risponde alle chiamate web
// Request mapping definisce l'indirizzo base per tutte le chiamate qui dentro
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    // Collego il service che è quello che fa i controlli
    @Autowired
    private TeamService teamService;

    // Crea team
    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestBody CreateTeamRequest request) {
        try {
            // passo la richiesta al service
            Team newTeam = teamService.createTeam(request);
            // se va tutto bene restituisco 200 ok con il team creato
            return ResponseEntity.ok(newTeam);
        } catch (RuntimeException e) {
            // se c'è un errore (es. nome duplicato) restituisco 400 bad request col messaggio
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteMember(@RequestBody InviteMemberRequest request) {
        try {
            // dico al service di mandare l'invito
            teamService.inviteMember(request);
            // restituisco un messaggio semplice per dire che è andata
            return ResponseEntity.ok("Invito inviato con successo!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/respond-invite")
    public ResponseEntity<?> respondToInvite(@RequestBody InviteMemberResponse request) {
        try {
            // passo la risposta dell'utente al service
            teamService.rispondiInvito(request);

            String messaggio = request.isAccetta() ? "Benvenuto nel team!" : "Invito rifiutato.";
            return ResponseEntity.ok(messaggio);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(teamService.getTeamById(teamId));
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<?> getMembers(@PathVariable Long teamId) {
        try {
            List<MemberDTO> members = teamService.getTeamMembers(teamId)
                    .stream()
                    .map(u -> new MemberDTO(u.getId(), u.getNome(), u.getCognome(), u.getEmail()))
                    .toList();

            return ResponseEntity.ok(members);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

public record MemberDTO(Long id, String nome, String cognome, String email) {}
}