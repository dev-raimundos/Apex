package br.app.coeur.apex.modules.authentication.application.refreshtoken;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenInput(@NotBlank(message = "Refresh token é obrigatório") String refreshToken) {
}
