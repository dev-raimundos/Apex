package br.app.coeur.apex.modules.authentication.infrastructure.web;

import br.app.coeur.apex.modules.authentication.application.refreshtoken.RefreshTokenInput;
import br.app.coeur.apex.modules.authentication.application.refreshtoken.RefreshTokenOutput;
import br.app.coeur.apex.modules.authentication.application.refreshtoken.RefreshTokenUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação")
public class RefreshTokenController {

    private final RefreshTokenUseCase refreshTokenUseCase;

    public RefreshTokenController(RefreshTokenUseCase refreshTokenUseCase) {
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    public record RefreshTokenRequest(@NotBlank(message = "Refresh token é obrigatório") String refreshToken) {
    }

    @PostMapping("/refresh")
    @Operation(summary = "Rotaciona o refresh token e emite um novo access token", description = "Público; limitado a 10 requisições por minuto por IP.")
    public RefreshTokenOutput refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenUseCase.execute(new RefreshTokenInput(request.refreshToken()));
    }
}
