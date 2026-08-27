package br.app.coeur.apex.modules.users.application.activateuser;

import java.util.UUID;

public record ActivateUserOutput(UUID id, boolean active) {
}
