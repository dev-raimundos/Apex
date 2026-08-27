package br.app.coeur.apex.shared.exception;

public final class AppConflictException extends AppException {

    public AppConflictException(String message) {
        super(message, ErrorType.CONFLICT);
    }
}
