package br.app.coeur.apex.modules.users.application.changepassword;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.infrastructure.repository.UserRepository;
import br.app.coeur.apex.shared.exception.AppNotFoundException;
import br.app.coeur.apex.shared.exception.AppUnauthorizedException;

@Service
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ChangePasswordOutput execute(UUID id, ChangePasswordInput input) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppNotFoundException("Usuário não encontrado: " + id));

        if (!passwordEncoder.matches(input.currentPassword(), user.getPasswordHash())) {
            throw new AppUnauthorizedException("Senha atual inválida.");
        }

        user.changePassword(passwordEncoder.encode(input.newPassword()));
        User saved = userRepository.save(user);

        return new ChangePasswordOutput(saved.getId());
    }
}
