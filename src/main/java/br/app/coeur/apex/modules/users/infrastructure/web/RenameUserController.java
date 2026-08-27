package br.app.coeur.apex.modules.users.infrastructure.web;

import java.util.UUID;

import br.app.coeur.apex.modules.users.application.renameuser.RenameUserInput;
import br.app.coeur.apex.modules.users.application.renameuser.RenameUserOutput;
import br.app.coeur.apex.modules.users.application.renameuser.RenameUserUseCase;
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
public class RenameUserController {

    private final RenameUserUseCase renameUserUseCase;
    private final CurrentUserGuard currentUserGuard;

    public RenameUserController(RenameUserUseCase renameUserUseCase, CurrentUserGuard currentUserGuard) {
        this.renameUserUseCase = renameUserUseCase;
        this.currentUserGuard = currentUserGuard;
    }

    public record RenameUserRequest(@NotBlank(message = "Nome é obrigatório") String newName) {
    }

    @PutMapping("/{id}/name")
    @Operation(summary = "Renomeia o usuário", description = "Somente o próprio usuário pode alterar.")
    public RenameUserOutput rename(@PathVariable UUID id, @Valid @RequestBody RenameUserRequest request) {
        currentUserGuard.ensureIsCurrentUser(id);
        return renameUserUseCase.execute(new RenameUserInput(id, request.newName()));
    }
}
