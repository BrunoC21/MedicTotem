package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.AsistenciaMedica;

@Repository
public interface AsistenciaRepository extends JpaRepository<AsistenciaMedica, Long> {
    Optional<AsistenciaMedica> findById(Long id);
    
}
