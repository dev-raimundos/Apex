package br.app.coeur.apex.modules.users.application.changepassword;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordInput(
        @NotBlank(message = "Senha atual é obrigatória") String currentPassword,
        @NotBlank(message = "Nova senha é obrigatória") String newPassword
) {
}
