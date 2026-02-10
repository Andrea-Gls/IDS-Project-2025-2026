package com.gasing.hackaton.identity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Iniezione dipendenze via costruttore
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Necessario per generare l'hash

    // +register(RegisterRequest req) : void
    @Transactional
    public void register(RegisterRequest req) {
        // Controllo se esiste già (come da UC 3)
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email già registrata");
        }

        User user = new User();
        user.setNome(req.nome());
        user.setCognome(req.cognome());
        user.setEmail(req.email());
        // Hash della password prima di salvare
        user.setPasswordHash(passwordEncoder.encode(req.password()));

        userRepository.save(user);
    }

    // +login(LoginRequest req) : String
    public String login(LoginRequest req) {
        // Recupero utente
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenziali non valide"));

        // Verifica password
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenziali non valide");
        }

        // Ritorna il token (String) come da diagramma. 
        // In produzione qui genereresti un JWT.
        return "mock-jwt-token-" + user.getId(); 
    }
}
