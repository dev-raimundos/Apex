package br.app.coeur.apex.modules.users.application.renameuser;

import java.util.UUID;

public record RenameUserInput(UUID id, String newName) {
}
