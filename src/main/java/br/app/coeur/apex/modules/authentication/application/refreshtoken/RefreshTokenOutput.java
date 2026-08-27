package br.app.coeur.apex.modules.authentication.application.refreshtoken;

import java.time.Instant;

public record RefreshTokenOutput(String accessToken, Instant expiresAt, String refreshToken) {
}
