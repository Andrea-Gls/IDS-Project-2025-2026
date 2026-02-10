package com.gasing.hackaton.identity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Corrisponde a LoginRequest nel diagramma
public record LoginRequest(
    @NotBlank @Email String email,
    @NotBlank String password
) {}