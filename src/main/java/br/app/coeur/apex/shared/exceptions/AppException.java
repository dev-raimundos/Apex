package br.app.coeur.apex.shared.exceptions;

public abstract class AppException extends RuntimeException {

    private final ErrorType errorType;

    protected AppException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
