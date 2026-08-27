package br.app.coeur.apex.modules.users.infrastructure.web;

import br.app.coeur.apex.modules.users.application.createuser.CreateUserInput;
import br.app.coeur.apex.modules.users.application.createuser.CreateUserOutput;
import br.app.coeur.apex.modules.users.application.createuser.CreateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários")
public class CreateUserController {

    private final CreateUserUseCase createUserUseCase;

    public CreateUserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    @Operation(summary = "Cria um novo usuário", description = "Endpoint público — não requer autenticação.")
    public ResponseEntity<CreateUserOutput> create(@Valid @RequestBody CreateUserInput input) {
        CreateUserOutput output = createUserUseCase.execute(input);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(URI.create("/api/users/" + output.id()))
                .body(output);
    }
}
