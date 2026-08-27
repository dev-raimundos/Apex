package br.app.coeur.apex.modules.authentication.domain.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import br.app.coeur.apex.modules.authentication.domain.LoginAttempt;

public final class LoginLockoutPolicy {

    public static final int MAX_FAILED_ATTEMPTS = 5;
    public static final Duration WINDOW = Duration.ofMinutes(15);

    private LoginLockoutPolicy() {
    }

    public static boolean isLockedOut(List<LoginAttempt> recentAttempts, Instant now) {
        List<LoginAttempt> failures = recentAttempts.stream()
                .filter(attempt -> !attempt.isSucceeded())
                .sorted(Comparator.comparing(LoginAttempt::getAttemptedAt).reversed())
                .toList();

        if (failures.size() < MAX_FAILED_ATTEMPTS) {
            return false;
        }

        Instant lockedUntil = failures.get(0).getAttemptedAt().plus(WINDOW);
        return now.isBefore(lockedUntil);
    }
}
