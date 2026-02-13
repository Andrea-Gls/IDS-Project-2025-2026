package com.gasing.hackhub.controller;

import com.gasing.hackhub.dto.hackathon.HackathonDTO;
import com.gasing.hackhub.model.Hackathon;
import com.gasing.hackhub.service.HackathonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hackathons")
public class HackathonController {

    @Autowired
    private HackathonService hackathonService;

    @PostMapping("/create")
    public ResponseEntity<?> createHackathon(@RequestBody HackathonDTO request) {
        try {
            // Chiamo il service che fa tutto (crea evento + assegna staff)
            Hackathon newHackathon = hackathonService.createHackathon(request);

            // Restituisco 200 OK e l'oggetto creato
            return ResponseEntity.ok(newHackathon);
        } catch (RuntimeException e) {
            // Se c'è un errore (es. nome duplicato, mentori mancanti), restituisco 400
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}