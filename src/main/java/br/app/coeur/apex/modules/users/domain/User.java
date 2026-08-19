package br.app.coeur.apex.modules.users.domain;

import java.time.Instant;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String passwordHash;
    private boolean active;
    private boolean emailVerified;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;

    private User() {
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
        user.updatedAt = Instant.now();
        return user;
    }

    public static User restore(
            UUID id,
            String name,
            String email,
            String passwordHash,
            boolean active,
            boolean emailVerified,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLoginAt) {
        User user = new User();
        user.id = id;
        user.name = name;
        user.email = email;
        user.passwordHash = passwordHash;
        user.active = active;
        user.emailVerified = emailVerified;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        user.lastLoginAt = lastLoginAt;
        return user;
    }

    public void changeEmail(String newEmail) {
        this.email = requireValid(newEmail);
        this.emailVerified = false;
        this.updatedAt = Instant.now();
    }

    public void verifyEmail() {
        this.emailVerified = true;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
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