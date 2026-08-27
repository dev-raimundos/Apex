package br.app.coeur.apex.modules.authentication.infrastructure.endpoints;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.app.coeur.apex.modules.authentication.application.logout.LogoutInput;
import br.app.coeur.apex.modules.authentication.application.logout.LogoutUseCase;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação")
public class LogoutController {

    private final LogoutUseCase logoutUseCase;

    public LogoutController(LogoutUseCase logoutUseCase) {
        this.logoutUseCase = logoutUseCase;
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoga um refresh token", description = "Público; idempotente — token desconhecido/já revogado retorna 204 do mesmo jeito.")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutInput input) {
        logoutUseCase.execute(input);
        return ResponseEntity.noContent().build();
    }
}
