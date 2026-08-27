package br.app.coeur.apex.modules.authentication.infrastructure.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.coeur.apex.modules.authentication.application.login.LoginInput;
import br.app.coeur.apex.modules.authentication.application.login.LoginOutput;
import br.app.coeur.apex.modules.authentication.application.login.LoginUseCase;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação")
public class LoginController {

    private final LoginUseCase loginUseCase;

    public LoginController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário", description = "Público; limitado a 10 requisições por minuto por IP.")
    public LoginOutput login(@Valid @RequestBody LoginInput input) {
        return loginUseCase.execute(input);
    }
}
