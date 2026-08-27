package br.app.coeur.apex.shared.exception;

public final class AppUnauthorizedException extends AppException {

    public AppUnauthorizedException(String message) {
        super(message, ErrorType.UNAUTHORIZED);
    }
}
