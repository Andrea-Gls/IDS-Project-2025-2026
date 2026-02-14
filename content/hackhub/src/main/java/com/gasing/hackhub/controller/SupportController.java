package com.gasing.hackhub.controller;

import com.gasing.hackhub.dto.staff.CreateSupportRequest;
import com.gasing.hackhub.model.SupportRequest;
import com.gasing.hackhub.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @Autowired
    private SupportService supportService;

    // Il Team chiede aiuto
    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody CreateSupportRequest request) {
        try {
            SupportRequest newRequest = supportService.createRequest(request);
            return ResponseEntity.ok(newRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resolve/{requestId}")
    public ResponseEntity<?> resolveRequest(
            @PathVariable Long requestId,
            @RequestParam Long mentorId,  // Chi sta rispondendo? (ID dell'Utente)
            @RequestParam String callLink // Il link della videochiamata
    ) {
        try {
            SupportRequest resolvedRequest = supportService.resolveRequest(requestId, mentorId, callLink);
            return ResponseEntity.ok("Richiesta risolta! Link inviato al team: " + resolvedRequest.getCallLink());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getTeamRequests(@PathVariable Long teamId) {
        List<SupportRequest> requests = supportService.getRequestsByTeam(teamId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/mentor/{userId}")
    public ResponseEntity<?> getMentorRequests(@PathVariable Long userId) {
        List<SupportRequest> requests = supportService.getRequestsByMentor(userId);
        return ResponseEntity.ok(requests);
    }
}
