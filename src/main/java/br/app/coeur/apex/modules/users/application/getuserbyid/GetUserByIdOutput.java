package br.app.coeur.apex.modules.users.application.getuserbyid;

import java.time.Instant;
import java.util.UUID;

public record GetUserByIdOutput(
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
