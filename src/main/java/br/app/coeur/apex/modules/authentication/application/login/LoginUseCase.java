package br.app.coeur.apex.modules.authentication.application.login;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import br.app.coeur.apex.modules.authentication.domain.LoginAttempt;
import br.app.coeur.apex.modules.authentication.domain.LoginAttemptRepository;
import br.app.coeur.apex.modules.authentication.domain.LoginLockoutPolicy;
import br.app.coeur.apex.modules.authentication.domain.RefreshToken;
import br.app.coeur.apex.modules.authentication.domain.RefreshTokenPolicy;
import br.app.coeur.apex.modules.authentication.domain.RefreshTokenRepository;
import br.app.coeur.apex.modules.authentication.infrastructure.security.JwtTokenGenerator;
import br.app.coeur.apex.modules.authentication.infrastructure.security.JwtTokenGenerator.GeneratedToken;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator;
import br.app.coeur.apex.modules.authentication.infrastructure.security.RefreshTokenGenerator.GeneratedRefreshToken;
import br.app.coeur.apex.shared.contracts.AuthenticatedUser;
import br.app.coeur.apex.shared.contracts.UserCredentialsChecker;
import br.app.coeur.apex.shared.exceptions.AppLockedException;
import br.app.coeur.apex.shared.exceptions.AppUnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final LoginAttemptRepository loginAttemptRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserCredentialsChecker userCredentialsChecker;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;

    public LoginUseCase(
            LoginAttemptRepository loginAttemptRepository,
            RefreshTokenRepository refreshTokenRepository,
            UserCredentialsChecker userCredentialsChecker,
            JwtTokenGenerator jwtTokenGenerator,
            RefreshTokenGenerator refreshTokenGenerator) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userCredentialsChecker = userCredentialsChecker;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    public LoginOutput execute(LoginInput input) {
        List<LoginAttempt> recentAttempts =
                loginAttemptRepository.findTop10ByEmailOrderByAttemptedAtDesc(input.email());
        if (LoginLockoutPolicy.isLockedOut(recentAttempts, Instant.now())) {
            throw new AppLockedException("Conta temporariamente bloqueada por excesso de tentativas de login.");
        }

        Optional<AuthenticatedUser> authenticatedUser =
                userCredentialsChecker.validate(input.email(), input.password());

        loginAttemptRepository.save(LoginAttempt.record(input.email(), authenticatedUser.isPresent()));

        AuthenticatedUser user = authenticatedUser
                .orElseThrow(() -> new AppUnauthorizedException("E-mail ou senha inválidos."));

        GeneratedToken accessToken = jwtTokenGenerator.generate(user);
        GeneratedRefreshToken refreshToken = refreshTokenGenerator.generate();
        refreshTokenRepository.save(RefreshToken.create(
                user.id(), user.email(), refreshToken.tokenHash(), RefreshTokenPolicy.LIFETIME));

        return new LoginOutput(accessToken.accessToken(), accessToken.expiresAt(), refreshToken.rawToken());
    }
}
