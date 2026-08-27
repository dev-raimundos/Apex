package br.app.coeur.apex.modules.users.application.changepassword;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.domain.UserRepository;
import br.app.coeur.apex.shared.exceptions.AppNotFoundException;
import br.app.coeur.apex.shared.exceptions.AppUnauthorizedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ChangePasswordOutput execute(ChangePasswordInput input) {
        User user = userRepository.findById(input.id())
                .orElseThrow(() -> new AppNotFoundException("Usuário não encontrado: " + input.id()));

        if (!passwordEncoder.matches(input.currentPassword(), user.getPasswordHash())) {
            throw new AppUnauthorizedException("Senha atual inválida.");
        }

        user.changePassword(passwordEncoder.encode(input.newPassword()));
        User saved = userRepository.save(user);

        return new ChangePasswordOutput(saved.getId());
    }
}
