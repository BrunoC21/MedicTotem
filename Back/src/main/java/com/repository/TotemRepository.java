package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.Totem;

@Repository
public interface TotemRepository extends JpaRepository<Totem, Long> {
    Optional<Totem> findById(Long id);
    Optional<Totem> findBySector(String sector);
}