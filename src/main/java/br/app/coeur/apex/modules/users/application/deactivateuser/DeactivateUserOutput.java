package br.app.coeur.apex.modules.users.application.deactivateuser;

import java.util.UUID;

public record DeactivateUserOutput(UUID id, boolean active) {
}
