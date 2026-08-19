package br.app.coeur.apex.modules.users.application.createuser;

import java.time.Instant;
import java.util.UUID;

public record CreateUserOutput(
        UUID id,
        String name,
        String email,
        Instant createdAt
) {
}
