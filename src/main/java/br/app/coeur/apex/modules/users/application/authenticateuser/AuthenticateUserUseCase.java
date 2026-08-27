package br.app.coeur.apex.modules.users.application.authenticateuser;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.infrastructure.repository.UserRepository;

/**
 * Called directly by the authentication package during login — one codebase, one dev, no boundary ceremony.
 * Not HTTP-facing, so it has no Input/Output DTOs of its own.
 */
@Service
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> execute(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(User::isActive)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPasswordHash()))
                .map(user -> {
                    user.registerLogin();
                    return userRepository.save(user);
                });
    }
}
