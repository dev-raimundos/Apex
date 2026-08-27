package br.app.coeur.apex.modules.users.application.getuserbyid;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.domain.UserRepository;
import br.app.coeur.apex.shared.exceptions.AppNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetUserByIdUseCase {

    private final UserRepository userRepository;

    public GetUserByIdUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GetUserByIdOutput execute(GetUserByIdInput input) {
        User user = userRepository.findById(input.id())
                .orElseThrow(() -> new AppNotFoundException("Usuário não encontrado: " + input.id()));

        return new GetUserByIdOutput(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.isActive(),
                user.isEmailVerified(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLoginAt());
    }
}
