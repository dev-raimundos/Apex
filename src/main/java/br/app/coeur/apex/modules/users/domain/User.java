package br.app.coeur.apex.modules.users.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users", schema = "users")
public class User {

    @Id
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "password_hash", nullable = false, columnDefinition = "text")
    private String passwordHash;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    protected User() {
    }

    public static User create(String name, String email, String passwordHash) {
        User user = new User();
        user.id = UUID.randomUUID();
        user.name = requireValid(name);
        user.email = requireValid(email);
        user.passwordHash = requireValid(passwordHash);
        user.active = true;
        user.emailVerified = false;
        user.createdAt = Instant.now();
        user.updatedAt = user.createdAt;
        return user;
    }

    public void rename(String newName) {
        this.name = requireValid(newName);
        this.updatedAt = Instant.now();
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = requireValid(newPasswordHash);
        this.updatedAt = Instant.now();
    }

    public void verifyEmail() {
        if (emailVerified) {
            return;
        }
        this.emailVerified = true;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (active) {
            return;
        }
        this.active = true;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        if (!active) {
            return;
        }
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public void registerLogin() {
        this.lastLoginAt = Instant.now();
    }

    private static String requireValid(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Campo obrigatório");
        }
        return value;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }
}
