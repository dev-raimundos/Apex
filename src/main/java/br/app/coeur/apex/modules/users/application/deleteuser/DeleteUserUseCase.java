package br.app.coeur.apex.modules.users.application.deleteuser;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.domain.UserNotFoundException;
import br.app.coeur.apex.modules.users.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(DeleteUserInput input) {
        User user = userRepository.findById(input.id())
                .orElseThrow(() -> new UserNotFoundException(input.id()));

        user.deactivate();
        userRepository.save(user);
    }
}
