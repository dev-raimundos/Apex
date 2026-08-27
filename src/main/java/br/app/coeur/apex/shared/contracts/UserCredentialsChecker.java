package br.app.coeur.apex.shared.contracts;

import java.util.Optional;

public interface UserCredentialsChecker {

    Optional<AuthenticatedUser> validate(String email, String password);
}
