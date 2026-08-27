package br.app.coeur.apex.shared.exceptions;

public final class AppUnauthorizedException extends AppException {

    public AppUnauthorizedException(String message) {
        super(message, ErrorType.UNAUTHORIZED);
    }
}
