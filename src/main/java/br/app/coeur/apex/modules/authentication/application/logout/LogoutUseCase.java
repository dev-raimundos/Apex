package br.app.coeur.apex.modules.authentication.application.logout;

import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.authentication.infrastructure.repository.RefreshTokenRepository;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator;

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
