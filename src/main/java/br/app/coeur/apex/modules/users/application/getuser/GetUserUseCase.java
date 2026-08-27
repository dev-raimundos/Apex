package br.app.coeur.apex.modules.users.application.getuser;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.app.coeur.apex.modules.users.domain.User;
import br.app.coeur.apex.modules.users.infrastructure.repository.UserRepository;
import br.app.coeur.apex.shared.exception.AppNotFoundException;

@Service
public class GetUserUseCase {

    private final UserRepository userRepository;

    public GetUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GetUserOutput execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppNotFoundException("Usuário não encontrado: " + id));

        return new GetUserOutput(
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
