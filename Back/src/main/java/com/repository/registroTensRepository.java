package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.registroTens;

@Repository
public interface registroTensRepository extends JpaRepository<registroTens, Long> {
    @SuppressWarnings("null")
    Optional<registroTens> findById(Long id);
    Optional<registroTens> findByRut(String rut);
}
