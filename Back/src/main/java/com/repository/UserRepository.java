package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findById(Long id);

  User findByRut(String rut);

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
