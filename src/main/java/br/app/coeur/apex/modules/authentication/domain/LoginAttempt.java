package br.app.coeur.apex.modules.authentication.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "login_attempts", schema = "auth")
public class LoginAttempt {

    @Id
    private UUID id;

    @Column(nullable = false, length = 320)
    private String email;

    @Column(nullable = false)
    private boolean succeeded;

    @Column(name = "attempted_at", nullable = false)
    private Instant attemptedAt;

    protected LoginAttempt() {
    }

    public static LoginAttempt record(String email, boolean succeeded) {
        return recordAt(email, succeeded, Instant.now());
    }

    /** Package-private: lets domain tests control {@code attemptedAt} without exposing it as public API. */
    static LoginAttempt recordAt(String email, boolean succeeded, Instant attemptedAt) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Campo obrigatório");
        }

        LoginAttempt attempt = new LoginAttempt();
        attempt.id = UUID.randomUUID();
        attempt.email = email;
        attempt.succeeded = succeeded;
        attempt.attemptedAt = attemptedAt;
        return attempt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public Instant getAttemptedAt() {
        return attemptedAt;
    }
}
