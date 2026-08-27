package br.app.coeur.apex.shared.exceptions;

public final class AppForbiddenException extends AppException {

    public AppForbiddenException(String message) {
        super(message, ErrorType.FORBIDDEN);
    }
}
