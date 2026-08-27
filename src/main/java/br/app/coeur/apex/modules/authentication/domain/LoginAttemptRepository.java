package br.app.coeur.apex.modules.authentication.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, UUID> {

    List<LoginAttempt> findTop10ByEmailOrderByAttemptedAtDesc(String email);
}
