package br.app.coeur.apex.modules.users.application.activateuser;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.infrastructure.repository.UserRepository;
import br.app.coeur.apex.shared.exception.AppNotFoundException;

@Service
public class ActivateUserUseCase {

    private final UserRepository userRepository;

    public ActivateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ActivateUserOutput execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppNotFoundException("Usuário não encontrado: " + id));

        user.activate();
        User saved = userRepository.save(user);

        return new ActivateUserOutput(saved.getId(), saved.isActive());
    }
}
