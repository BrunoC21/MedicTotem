package com.repository;

import com.models.Totem;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotemRepository extends JpaRepository<Totem, Long> {
    Optional<Totem> findById(Long id);
}