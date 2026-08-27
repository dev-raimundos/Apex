package br.app.coeur.apex.shared.contracts;

import java.util.UUID;

public record AuthenticatedUser(UUID id, String email) {
}
