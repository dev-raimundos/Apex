package br.app.coeur.apex.shared.security;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import br.app.coeur.apex.shared.exceptions.AppForbiddenException;

@Component
public class CurrentUserGuard {

    public void ensureIsCurrentUser(UUID id) {
        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UUID currentUserId = UUID.fromString(authentication.getToken().getSubject());
        if (!currentUserId.equals(id)) {
            throw new AppForbiddenException("Você não tem permissão para acessar este recurso.");
        }
    }
}
