package br.app.coeur.apex.modules.authentication.application.login;

import jakarta.validation.constraints.NotBlank;

public record LoginInput(
        @NotBlank(message = "E-mail é obrigatório") String email,
        @NotBlank(message = "Senha é obrigatória") String password
) {
}
