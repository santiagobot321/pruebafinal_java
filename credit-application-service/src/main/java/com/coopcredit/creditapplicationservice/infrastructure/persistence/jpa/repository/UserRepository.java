package com.coopcredit.creditapplicationservice.infrastructure.persistence.jpa.repository;

import com.coopcredit.creditapplicationservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
