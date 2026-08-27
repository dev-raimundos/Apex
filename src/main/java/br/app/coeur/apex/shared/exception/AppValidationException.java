package br.app.coeur.apex.shared.exception;

public final class AppValidationException extends AppException {

    public AppValidationException(String message) {
        super(message, ErrorType.VALIDATION);
    }
}
