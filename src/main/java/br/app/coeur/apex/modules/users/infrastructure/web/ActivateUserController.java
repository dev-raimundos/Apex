package br.app.coeur.apex.modules.users.infrastructure.web;

import java.util.UUID;

import br.app.coeur.apex.modules.users.application.activateuser.ActivateUserOutput;
import br.app.coeur.apex.modules.users.application.activateuser.ActivateUserUseCase;
import br.app.coeur.apex.shared.security.CurrentUserGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários")
public class ActivateUserController {

    private final ActivateUserUseCase activateUserUseCase;
    private final CurrentUserGuard currentUserGuard;

    public ActivateUserController(ActivateUserUseCase activateUserUseCase, CurrentUserGuard currentUserGuard) {
        this.activateUserUseCase = activateUserUseCase;
        this.currentUserGuard = currentUserGuard;
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Ativa o usuário", description = "Idempotente; somente o próprio usuário pode acessar.")
    public ActivateUserOutput activate(@PathVariable UUID id) {
        currentUserGuard.ensureIsCurrentUser(id);
        return activateUserUseCase.execute(id);
    }
}
