package br.app.coeur.apex.modules.users.infrastructure.web;

import br.app.coeur.apex.modules.users.application.createuser.CreateUserInput;
import br.app.coeur.apex.modules.users.application.createuser.CreateUserOutput;
import br.app.coeur.apex.modules.users.application.createuser.CreateUserUseCase;
import br.app.coeur.apex.modules.users.application.deleteuser.DeleteUserInput;
import br.app.coeur.apex.modules.users.application.deleteuser.DeleteUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUserOutput create(@Valid @RequestBody CreateUserInput input) {
        return createUserUseCase.execute(input);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUserUseCase.execute(new DeleteUserInput(id));
        return ResponseEntity.noContent().build();
    }
}
