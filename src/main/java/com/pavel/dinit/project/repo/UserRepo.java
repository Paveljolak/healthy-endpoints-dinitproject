package com.pavel.dinit.project.repo;

import com.pavel.dinit.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String name);

    Optional<User> findById(Long addedByUserId);

    @Transactional
    void deleteById(Long id);

    Optional<User> findByEmail(String email);
}