package br.app.coeur.apex.modules.users.application.verifyemail;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.infrastructure.repository.UserRepository;
import br.app.coeur.apex.shared.exception.AppNotFoundException;

@Service
public class VerifyEmailUseCase {

    private final UserRepository userRepository;

    public VerifyEmailUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public VerifyEmailOutput execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppNotFoundException("Usuário não encontrado: " + id));

        user.verifyEmail();
        User saved = userRepository.save(user);

        return new VerifyEmailOutput(saved.getId(), saved.isEmailVerified());
    }
}
