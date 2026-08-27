package br.app.coeur.apex.shared.exceptions;

public final class AppNotFoundException extends AppException {

    public AppNotFoundException(String message) {
        super(message, ErrorType.NOT_FOUND);
    }
}
