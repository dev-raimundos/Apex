package br.app.coeur.apex.modules.users.infrastructure.endpoints;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.coeur.apex.modules.authentication.infrastructure.security.CurrentUserGuard;
import br.app.coeur.apex.modules.users.application.getuser.GetUserOutput;
import br.app.coeur.apex.modules.users.application.getuser.GetUserUseCase;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários")
public class GetUserController {

    private final GetUserUseCase getUserUseCase;
    private final CurrentUserGuard currentUserGuard;

    public GetUserController(GetUserUseCase getUserUseCase, CurrentUserGuard currentUserGuard) {
        this.getUserUseCase = getUserUseCase;
        this.currentUserGuard = currentUserGuard;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário pelo id", description = "Somente o próprio usuário pode acessar.")
    public GetUserOutput getById(@PathVariable UUID id) {
        currentUserGuard.ensureIsCurrentUser(id);
        return getUserUseCase.execute(id);
    }
}
