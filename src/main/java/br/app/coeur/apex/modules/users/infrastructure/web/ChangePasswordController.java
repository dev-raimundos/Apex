package br.app.coeur.apex.modules.users.infrastructure.web;

import java.util.UUID;

import br.app.coeur.apex.modules.users.application.changepassword.ChangePasswordInput;
import br.app.coeur.apex.modules.users.application.changepassword.ChangePasswordOutput;
import br.app.coeur.apex.modules.users.application.changepassword.ChangePasswordUseCase;
import br.app.coeur.apex.shared.security.CurrentUserGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários")
public class ChangePasswordController {

    private final ChangePasswordUseCase changePasswordUseCase;
    private final CurrentUserGuard currentUserGuard;

    public ChangePasswordController(ChangePasswordUseCase changePasswordUseCase, CurrentUserGuard currentUserGuard) {
        this.changePasswordUseCase = changePasswordUseCase;
        this.currentUserGuard = currentUserGuard;
    }

    public record ChangePasswordBody(
            @NotBlank(message = "Senha atual é obrigatória") String currentPassword,
            @NotBlank(message = "Nova senha é obrigatória") String newPassword
    ) {
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "Altera a senha do usuário", description = "Requer a senha atual; somente o próprio usuário pode alterar.")
    public ChangePasswordOutput changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordBody body) {
        currentUserGuard.ensureIsCurrentUser(id);
        return changePasswordUseCase.execute(new ChangePasswordInput(id, body.currentPassword(), body.newPassword()));
    }
}
