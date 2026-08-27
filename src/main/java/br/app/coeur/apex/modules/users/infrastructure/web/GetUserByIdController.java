package br.app.coeur.apex.modules.users.infrastructure.web;

import java.util.UUID;

import br.app.coeur.apex.modules.users.application.getuserbyid.GetUserByIdInput;
import br.app.coeur.apex.modules.users.application.getuserbyid.GetUserByIdOutput;
import br.app.coeur.apex.modules.users.application.getuserbyid.GetUserByIdUseCase;
import br.app.coeur.apex.shared.security.CurrentUserGuard;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários")
public class GetUserByIdController {

    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CurrentUserGuard currentUserGuard;

    public GetUserByIdController(GetUserByIdUseCase getUserByIdUseCase, CurrentUserGuard currentUserGuard) {
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.currentUserGuard = currentUserGuard;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário pelo id", description = "Somente o próprio usuário pode acessar.")
    public GetUserByIdOutput getById(@PathVariable UUID id) {
        currentUserGuard.ensureIsCurrentUser(id);
        return getUserByIdUseCase.execute(new GetUserByIdInput(id));
    }
}
