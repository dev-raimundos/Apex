package br.app.coeur.apex.modules.users.infrastructure.web;

import java.util.UUID;

import br.app.coeur.apex.modules.users.application.verifyemail.VerifyEmailOutput;
import br.app.coeur.apex.modules.users.application.verifyemail.VerifyEmailUseCase;
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
public class VerifyEmailController {

    private final VerifyEmailUseCase verifyEmailUseCase;
    private final CurrentUserGuard currentUserGuard;

    public VerifyEmailController(VerifyEmailUseCase verifyEmailUseCase, CurrentUserGuard currentUserGuard) {
        this.verifyEmailUseCase = verifyEmailUseCase;
        this.currentUserGuard = currentUserGuard;
    }

    @PostMapping("/{id}/verify-email")
    @Operation(summary = "Marca o e-mail do usuário como verificado", description = "Idempotente; somente o próprio usuário pode acessar.")
    public VerifyEmailOutput verifyEmail(@PathVariable UUID id) {
        currentUserGuard.ensureIsCurrentUser(id);
        return verifyEmailUseCase.execute(id);
    }
}
