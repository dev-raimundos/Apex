package br.app.coeur.apex.modules.users.domain;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super("E-mail já está em uso: " + email);
    }
}
