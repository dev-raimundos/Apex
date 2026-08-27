package br.app.coeur.apex.modules.authentication.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.app.coeur.apex.modules.authentication.domain.LoginAttempt;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, UUID> {

    List<LoginAttempt> findTop10ByEmailOrderByAttemptedAtDesc(String email);
}
