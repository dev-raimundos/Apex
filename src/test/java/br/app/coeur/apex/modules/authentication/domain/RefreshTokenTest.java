package br.app.coeur.apex.modules.authentication.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class RefreshTokenTest {

    @Test
    void isActiveWhenNotRevokedAndNotExpired() {
        RefreshToken token = RefreshToken.create(UUID.randomUUID(), "ana@example.com", "hash", Duration.ofDays(7));

        assertThat(token.isActive()).isTrue();
    }

    @Test
    void isNotActiveWhenExpired() {
        RefreshToken token = RefreshToken.create(UUID.randomUUID(), "ana@example.com", "hash", Duration.ofSeconds(-1));

        assertThat(token.isActive()).isFalse();
    }

    @Test
    void revokeIsIdempotentAndKeepsFirstTimestamp() {
        RefreshToken token = RefreshToken.create(UUID.randomUUID(), "ana@example.com", "hash", Duration.ofDays(7));

        token.revoke();
        var firstRevokedAt = token.getRevokedAt();
        token.revoke();

        assertThat(token.getRevokedAt()).isEqualTo(firstRevokedAt);
        assertThat(token.isActive()).isFalse();
    }
}
