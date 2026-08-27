package br.app.coeur.apex.shared.exception;

public final class AppLockedException extends AppException {

    public AppLockedException(String message) {
        super(message, ErrorType.LOCKED);
    }
}
