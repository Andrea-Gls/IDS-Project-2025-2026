package com.gasing.hackhub.service;

import com.gasing.hackhub.dto.auth.request.LoginRequest;
import com.gasing.hackhub.dto.auth.request.RegisterRequest;
import com.gasing.hackhub.dto.auth.response.AuthResponse;
import com.gasing.hackhub.dto.auth.response.UserResponse;
import com.gasing.hackhub.model.User;
import com.gasing.hackhub.repository.UserRepository;
import com.gasing.hackhub.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Errore: Email già in uso!");
        }

        User newUser = new User();
        newUser.setNome(request.getNome());
        newUser.setCognome(request.getCognome());
        newUser.setEmail(request.getEmail());

        // Minimo per ora: password in chiaro (poi BCrypt)
        newUser.setPasswordHash(request.getPassword());

        User savedUser = userRepository.save(newUser);
        return mapToUserResponse(savedUser);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Login fallito: Credenziali errate"));

        if (!request.getPassword().equals(user.getPasswordHash())) {
            throw new RuntimeException("Login fallito: Credenziali errate");
        }

        String token = jwtService.generateToken(user.getEmail());

        AuthResponse res = new AuthResponse();
        res.setToken(token);
        res.setUser(mapToUserResponse(user));
        return res;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNome(user.getNome());
        response.setCognome(user.getCognome());
        response.setEmail(user.getEmail());
        return response;
    }
}
