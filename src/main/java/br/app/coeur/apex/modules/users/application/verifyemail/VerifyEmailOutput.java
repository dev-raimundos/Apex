package br.app.coeur.apex.modules.users.application.verifyemail;

import java.util.UUID;

public record VerifyEmailOutput(UUID id, boolean emailVerified) {
}
