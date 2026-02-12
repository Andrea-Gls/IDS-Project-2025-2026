package com.gasing.hackhub.service;

import com.gasing.hackhub.dto.auth.request.RegisterRequest;
import com.gasing.hackhub.dto.auth.request.LoginRequest;
import com.gasing.hackhub.dto.auth.response.UserResponse;
import com.gasing.hackhub.model.User;
import com.gasing.hackhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse register(RegisterRequest request) {             // metodo per registrarsi

        // Controllo se l'email esiste già
        // Se c'è già un utente con questa mail, blocco tutto e lancio un errore.
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Errore: Email già in uso!");
        }

        // Altrimenti creo l'Entity User dai dati della Request
        // Passo i dati dal DTO (JSON) all'Entità (Database)
        User newUser = new User();
        newUser.setNome(request.getNome());
        newUser.setCognome(request.getCognome());
        newUser.setEmail(request.getEmail());

        // Gestione Password
        // Qui dovrei criptarla (BCrypt) per ora la salviamo così com'è per testare
        newUser.setPasswordHash(request.getPassword());

        // Salvo nel Database
        // Il metodo save restituisce l'utente aggiornato con id creato dal DB.
        User savedUser = userRepository.save(newUser);

        // Converto in DTO di risposta
        // Non restituisco 'savedUser' perché contiene la password.
        // Creo un oggetto pulito 'UserResponse' solo con i dati pubblici.
        return mapToUserResponse(savedUser);
    }

    public UserResponse login(LoginRequest request) {                   // metodo di login

        // Cerco l'utente per Email
        // Se non lo trovo, lancio un errore generico (per sicurezza non diciamo "email non trovata").
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Login fallito: Credenziali errate"));

        // Controllo la Password
        // Confronto quella inviata con quella nel DB.
        if (!request.getPassword().equals(user.getPasswordHash())) {
            throw new RuntimeException("Login fallito: Credenziali errate");
        }

        // Se tutto ok, restituisco i dati dell'utente (senza password)
        return mapToUserResponse(user);
    }

    // Metodo privato
    private UserResponse mapToUserResponse(User user) {    // Serve per non riscrivere il codice di conversione due volte
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNome(user.getNome());
        response.setCognome(user.getCognome());
        response.setEmail(user.getEmail());
        return response;
    }
}