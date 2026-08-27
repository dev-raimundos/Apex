package br.app.coeur.apex.modules.authentication.application.refreshtoken;

import br.app.coeur.apex.modules.authentication.domain.RefreshToken;
import br.app.coeur.apex.modules.authentication.domain.RefreshTokenPolicy;
import br.app.coeur.apex.modules.authentication.domain.RefreshTokenRepository;
import br.app.coeur.apex.modules.authentication.infrastructure.security.JwtTokenGenerator;
import br.app.coeur.apex.modules.authentication.infrastructure.security.JwtTokenGenerator.GeneratedToken;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator.GeneratedRefreshToken;
import br.app.coeur.apex.shared.contracts.AuthenticatedUser;
import br.app.coeur.apex.shared.exceptions.AppUnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenUseCase {

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

        AuthenticatedUser user = new AuthenticatedUser(current.getUserId(), current.getEmail());

        GeneratedToken accessToken = jwtTokenGenerator.generate(user);
        GeneratedRefreshToken newRefreshToken = refreshTokenGenerator.generate();
        refreshTokenRepository.save(RefreshToken.create(
                user.id(), user.email(), newRefreshToken.tokenHash(), RefreshTokenPolicy.LIFETIME));

        return new RefreshTokenOutput(accessToken.accessToken(), accessToken.expiresAt(), newRefreshToken.rawToken());
    }
}
