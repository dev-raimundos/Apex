package br.app.coeur.apex.modules.users.application.deactivateuser;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.infrastructure.repository.UserRepository;
import br.app.coeur.apex.shared.exception.AppNotFoundException;

@Service
public class DeactivateUserUseCase {

    private final UserRepository userRepository;

    public DeactivateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DeactivateUserOutput execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppNotFoundException("Usuário não encontrado: " + id));

        user.deactivate();
        User saved = userRepository.save(user);

        return new DeactivateUserOutput(saved.getId(), saved.isActive());
    }
}
