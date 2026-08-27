package br.app.coeur.apex.modules.users.infrastructure.web;

import java.util.Optional;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.domain.UserRepository;
import br.app.coeur.apex.shared.contracts.AuthenticatedUser;
import br.app.coeur.apex.shared.contracts.UserCredentialsChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserCredentialsCheckerImpl implements UserCredentialsChecker {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCredentialsCheckerImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<AuthenticatedUser> validate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.isActive())
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .map(this::registerLoginAndMap);
    }

    private AuthenticatedUser registerLoginAndMap(User user) {
        user.registerLogin();
        userRepository.save(user);
        return new AuthenticatedUser(user.getId(), user.getEmail());
    }
}
