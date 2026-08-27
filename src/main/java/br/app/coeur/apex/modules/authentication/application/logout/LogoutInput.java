package br.app.coeur.apex.modules.authentication.application.logout;

import jakarta.validation.constraints.NotBlank;

public record LogoutInput(@NotBlank(message = "Refresh token é obrigatório") String refreshToken) {
}
