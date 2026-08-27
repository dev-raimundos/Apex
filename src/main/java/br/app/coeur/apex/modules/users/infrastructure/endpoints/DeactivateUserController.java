package br.app.coeur.apex.modules.users.infrastructure.endpoints;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.coeur.apex.modules.authentication.infrastructure.security.CurrentUserGuard;
import br.app.coeur.apex.modules.users.application.deactivateuser.DeactivateUserOutput;
import br.app.coeur.apex.modules.users.application.deactivateuser.DeactivateUserUseCase;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários")
public class DeactivateUserController {

    private final DeactivateUserUseCase deactivateUserUseCase;
    private final CurrentUserGuard currentUserGuard;

    public DeactivateUserController(DeactivateUserUseCase deactivateUserUseCase, CurrentUserGuard currentUserGuard) {
        this.deactivateUserUseCase = deactivateUserUseCase;
        this.currentUserGuard = currentUserGuard;
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Desativa o usuário", description = "Idempotente; somente o próprio usuário pode acessar.")
    public DeactivateUserOutput deactivate(@PathVariable UUID id) {
        currentUserGuard.ensureIsCurrentUser(id);
        return deactivateUserUseCase.execute(id);
    }
}
