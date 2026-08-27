package br.app.coeur.apex.modules.users.application.renameuser;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.domain.UserRepository;
import br.app.coeur.apex.shared.exceptions.AppNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RenameUserUseCase {

    private final UserRepository userRepository;

    public RenameUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RenameUserOutput execute(RenameUserInput input) {
        User user = userRepository.findById(input.id())
                .orElseThrow(() -> new AppNotFoundException("Usuário não encontrado: " + input.id()));

        user.rename(input.newName());
        User saved = userRepository.save(user);

        return new RenameUserOutput(saved.getId(), saved.getName());
    }
}
