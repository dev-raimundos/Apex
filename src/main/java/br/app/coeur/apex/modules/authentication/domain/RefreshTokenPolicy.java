package br.app.coeur.apex.modules.authentication.domain;

import java.time.Duration;

public final class RefreshTokenPolicy {

    public static final Duration LIFETIME = Duration.ofDays(7);

    private RefreshTokenPolicy() {
    }
}
