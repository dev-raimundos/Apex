package br.app.coeur.apex.modules.authentication.application.login;

import java.time.Instant;

public record LoginOutput(String accessToken, Instant expiresAt, String refreshToken) {
}
