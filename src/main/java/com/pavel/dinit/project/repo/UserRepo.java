package com.pavel.dinit.project.repo;

import com.pavel.dinit.project.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String name);
    Optional<User> findById(Long id);


    @Transactional
    void deleteById(Long id);

    Optional<User> findByEmail(String email);
}