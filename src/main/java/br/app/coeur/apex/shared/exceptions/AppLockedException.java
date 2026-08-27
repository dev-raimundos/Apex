package br.app.coeur.apex.shared.exceptions;

public final class AppLockedException extends AppException {

    public AppLockedException(String message) {
        super(message, ErrorType.LOCKED);
    }
}
