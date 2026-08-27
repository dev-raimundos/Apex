package br.app.coeur.apex.modules.users.application.createuser;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.domain.UserRepository;
import br.app.coeur.apex.shared.exceptions.AppConflictException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CreateUserOutput execute(CreateUserInput input) {
        userRepository.findByEmail(input.email()).ifPresent(existing -> {
            throw new AppConflictException("E-mail já está em uso: " + input.email());
        });

        User user = User.create(input.name(), input.email(), passwordEncoder.encode(input.password()));
        User saved = userRepository.save(user);

        return new CreateUserOutput(saved.getId(), saved.getName(), saved.getEmail(), saved.getCreatedAt());
    }
}
