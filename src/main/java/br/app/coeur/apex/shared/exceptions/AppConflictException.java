package br.app.coeur.apex.shared.exceptions;

public final class AppConflictException extends AppException {

    public AppConflictException(String message) {
        super(message, ErrorType.CONFLICT);
    }
}
