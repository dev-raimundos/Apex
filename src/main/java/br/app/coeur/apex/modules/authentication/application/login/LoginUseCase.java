package br.app.coeur.apex.modules.authentication.application.login;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.authentication.domain.LoginAttempt;
import br.app.coeur.apex.modules.authentication.domain.RefreshToken;
import br.app.coeur.apex.modules.authentication.domain.service.LoginLockoutPolicy;
import br.app.coeur.apex.modules.authentication.infrastructure.repository.LoginAttemptRepository;
import br.app.coeur.apex.modules.authentication.infrastructure.repository.RefreshTokenRepository;
import br.app.coeur.apex.modules.authentication.infrastructure.security.JwtTokenGenerator;
import br.app.coeur.apex.modules.authentication.infrastructure.security.JwtTokenGenerator.GeneratedToken;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator.GeneratedRefreshToken;
import br.app.coeur.apex.modules.users.application.authenticateuser.AuthenticateUserUseCase;
import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.shared.exception.AppLockedException;
import br.app.coeur.apex.shared.exception.AppUnauthorizedException;

@Service
public class LoginUseCase {

    private static final Duration REFRESH_TOKEN_LIFETIME = Duration.ofDays(7);

    private final LoginAttemptRepository loginAttemptRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

    public LoginUseCase(
            LoginAttemptRepository loginAttemptRepository,
            RefreshTokenRepository refreshTokenRepository,
            AuthenticateUserUseCase authenticateUserUseCase,
            JwtTokenGenerator jwtTokenGenerator,
            RefreshTokenGenerator refreshTokenGenerator) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    public LoginOutput execute(LoginInput input) {
        List<LoginAttempt> recentAttempts =
                loginAttemptRepository.findTop10ByEmailOrderByAttemptedAtDesc(input.email());
        if (LoginLockoutPolicy.isLockedOut(recentAttempts, Instant.now())) {
            throw new AppLockedException("Conta temporariamente bloqueada por excesso de tentativas de login.");
        }

        Optional<User> authenticatedUser = authenticateUserUseCase.execute(input.email(), input.password());

        loginAttemptRepository.save(LoginAttempt.record(input.email(), authenticatedUser.isPresent()));

        User user = authenticatedUser
                .orElseThrow(() -> new AppUnauthorizedException("E-mail ou senha inválidos."));

        return issueTokens(user.getId(), user.getEmail());
    }

    private LoginOutput issueTokens(UUID userId, String email) {
        GeneratedToken accessToken = jwtTokenGenerator.generate(userId, email);
        GeneratedRefreshToken refreshToken = refreshTokenGenerator.generate();
        refreshTokenRepository.save(RefreshToken.create(userId, email, refreshToken.tokenHash(), REFRESH_TOKEN_LIFETIME));

        return new LoginOutput(accessToken.accessToken(), accessToken.expiresAt(), refreshToken.rawToken());
    }
}
