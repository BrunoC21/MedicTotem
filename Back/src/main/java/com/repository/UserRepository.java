package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @SuppressWarnings("null")
  Optional<User> findById(Long id);
  
  List<User> findByRoles_Name(String roleName);
    
  List<User> findByInstrumento(String instrumento);
  User findByRut(String rut);

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
