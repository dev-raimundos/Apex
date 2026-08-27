package br.app.coeur.apex.modules.users.application.getuser;

import java.time.Instant;
import java.util.UUID;

public record GetUserOutput(
        UUID id,
        String name,
        String email,
        boolean active,
        boolean emailVerified,
        Instant createdAt,
        Instant updatedAt,
        Instant lastLoginAt
) {
}
