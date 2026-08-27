package br.app.coeur.apex.modules.authentication.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class LoginLockoutPolicyTest {

    private static final String EMAIL = "ana@example.com";

    @Test
    void notLockedOutWithFewerThanMaxFailures() {
        Instant now = Instant.now();
        List<LoginAttempt> attempts = attemptsAgo(now, false, 1, 2, 3, 4);

        assertThat(LoginLockoutPolicy.isLockedOut(attempts, now)).isFalse();
    }

    @Test
    void lockedOutAfterMaxFailuresWithinWindow() {
        Instant now = Instant.now();
        List<LoginAttempt> attempts = attemptsAgo(now, false, 1, 2, 3, 4, 5);

        assertThat(LoginLockoutPolicy.isLockedOut(attempts, now)).isTrue();
    }

    @Test
    void notLockedOutOnceWindowHasPassed() {
        Instant now = Instant.now();
        List<LoginAttempt> attempts = attemptsAgoMinutes(now, false, 20, 21, 22, 23, 24);

        assertThat(LoginLockoutPolicy.isLockedOut(attempts, now)).isFalse();
    }

    @Test
    void successfulAttemptsDoNotCountTowardLockout() {
        Instant now = Instant.now();
        List<LoginAttempt> attempts = new ArrayList<>();
        attempts.addAll(attemptsAgo(now, true, 1, 2, 3, 4, 5));
        attempts.addAll(attemptsAgo(now, false, 1, 2));

        assertThat(LoginLockoutPolicy.isLockedOut(attempts, now)).isFalse();
    }

    private List<LoginAttempt> attemptsAgo(Instant now, boolean succeeded, int... secondsAgo) {
        List<LoginAttempt> attempts = new ArrayList<>();
        for (int seconds : secondsAgo) {
            attempts.add(recordAt(now.minusSeconds(seconds), succeeded));
        }
        return attempts;
    }

    private List<LoginAttempt> attemptsAgoMinutes(Instant now, boolean succeeded, int... minutesAgo) {
        List<LoginAttempt> attempts = new ArrayList<>();
        for (int minutes : minutesAgo) {
            attempts.add(recordAt(now.minusSeconds(minutes * 60L), succeeded));
        }
        return attempts;
    }

    private LoginAttempt recordAt(Instant attemptedAt, boolean succeeded) {
        return LoginAttempt.recordAt(EMAIL, succeeded, attemptedAt);
    }
}
