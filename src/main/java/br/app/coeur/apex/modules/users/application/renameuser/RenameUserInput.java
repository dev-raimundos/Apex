package br.app.coeur.apex.modules.users.application.renameuser;

import jakarta.validation.constraints.NotBlank;

public record RenameUserInput(@NotBlank(message = "Nome é obrigatório") String newName) {
}
