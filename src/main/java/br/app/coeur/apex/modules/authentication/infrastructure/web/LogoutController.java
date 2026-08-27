package br.app.coeur.apex.modules.authentication.infrastructure.web;

import br.app.coeur.apex.modules.authentication.application.logout.LogoutInput;
import br.app.coeur.apex.modules.authentication.application.logout.LogoutUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação")
public class LogoutController {

    private final LogoutUseCase logoutUseCase;

    public LogoutController(LogoutUseCase logoutUseCase) {
        this.logoutUseCase = logoutUseCase;
    }

    public record LogoutRequest(@NotBlank(message = "Refresh token é obrigatório") String refreshToken) {
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoga um refresh token", description = "Público; idempotente — token desconhecido/já revogado retorna 204 do mesmo jeito.")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        logoutUseCase.execute(new LogoutInput(request.refreshToken()));
        return ResponseEntity.noContent().build();
    }
}
