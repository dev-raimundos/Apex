package br.app.coeur.apex.modules.users.application.activateuser;

import java.util.UUID;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.domain.UserRepository;
import br.app.coeur.apex.shared.exceptions.AppNotFoundException;
import org.springframework.stereotype.Service;

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
