package br.app.coeur.apex.modules.authentication.application.refreshtoken;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.authentication.domain.RefreshToken;
import br.app.coeur.apex.modules.authentication.infrastructure.repository.RefreshTokenRepository;
import br.app.coeur.apex.modules.authentication.infrastructure.security.JwtTokenGenerator;
import br.app.coeur.apex.modules.authentication.infrastructure.security.JwtTokenGenerator.GeneratedToken;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator.GeneratedRefreshToken;
import br.app.coeur.apex.shared.exception.AppUnauthorizedException;

@Service
public class RefreshTokenUseCase {

    private static final Duration REFRESH_TOKEN_LIFETIME = Duration.ofDays(7);

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

    public RefreshTokenUseCase(
            RefreshTokenRepository refreshTokenRepository,
            JwtTokenGenerator jwtTokenGenerator,
            RefreshTokenGenerator refreshTokenGenerator) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    public RefreshTokenOutput execute(RefreshTokenInput input) {
        String tokenHash = refreshTokenGenerator.hash(input.refreshToken());
        RefreshToken current = refreshTokenRepository.findByTokenHash(tokenHash)
                .filter(RefreshToken::isActive)
                .orElseThrow(() -> new AppUnauthorizedException("Refresh token inválido ou expirado."));

        current.revoke();
        refreshTokenRepository.save(current);

        UUID userId = current.getUserId();
        String email = current.getEmail();

        GeneratedToken accessToken = jwtTokenGenerator.generate(userId, email);
        GeneratedRefreshToken newRefreshToken = refreshTokenGenerator.generate();
        refreshTokenRepository.save(RefreshToken.create(userId, email, newRefreshToken.tokenHash(), REFRESH_TOKEN_LIFETIME));

        return new RefreshTokenOutput(accessToken.accessToken(), accessToken.expiresAt(), newRefreshToken.rawToken());
    }
}
