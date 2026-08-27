package br.app.coeur.apex.modules.authentication.application.logout;

import br.app.coeur.apex.modules.authentication.domain.RefreshTokenRepository;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator;
import org.springframework.stereotype.Service;

@Service
public class LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenGenerator refreshTokenGenerator;

    public LogoutUseCase(RefreshTokenRepository refreshTokenRepository, RefreshTokenGenerator refreshTokenGenerator) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    public void execute(LogoutInput input) {
        String tokenHash = refreshTokenGenerator.hash(input.refreshToken());
        refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(token -> {
            token.revoke();
            refreshTokenRepository.save(token);
        });
    }
}
