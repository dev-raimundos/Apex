package br.app.coeur.apex.modules.users.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void createsActiveAndUnverifiedByDefault() {
        User user = User.create("Ana", "ana@example.com", "hashed-password");

        assertThat(user.isActive()).isTrue();
        assertThat(user.isEmailVerified()).isFalse();
        assertThat(user.getLastLoginAt()).isNull();
    }

    @Test
    void createRejectsBlankFields() {
        assertThatThrownBy(() -> User.create(" ", "ana@example.com", "hash"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> User.create("Ana", "", "hash"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> User.create("Ana", "ana@example.com", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void renameRejectsBlankName() {
        User user = User.create("Ana", "ana@example.com", "hash");

        assertThatThrownBy(() -> user.rename(" "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void verifyEmailIsIdempotent() {
        User user = User.create("Ana", "ana@example.com", "hash");

        user.verifyEmail();
        var updatedAtAfterFirstCall = user.getUpdatedAt();
        user.verifyEmail();

        assertThat(user.isEmailVerified()).isTrue();
        assertThat(user.getUpdatedAt()).isEqualTo(updatedAtAfterFirstCall);
    }

    @Test
    void deactivateThenActivateIsIdempotent() {
        User user = User.create("Ana", "ana@example.com", "hash");

        user.deactivate();
        assertThat(user.isActive()).isFalse();
        user.deactivate();
        assertThat(user.isActive()).isFalse();

        user.activate();
        assertThat(user.isActive()).isTrue();
        user.activate();
        assertThat(user.isActive()).isTrue();
    }

    @Test
    void registerLoginSetsLastLoginAtWithoutTouchingUpdatedAt() {
        User user = User.create("Ana", "ana@example.com", "hash");
        var createdUpdatedAt = user.getUpdatedAt();

        user.registerLogin();

        assertThat(user.getLastLoginAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isEqualTo(createdUpdatedAt);
    }
}
