package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.CitaSimple;

@Repository
public interface CitaSimpleRepository extends JpaRepository<CitaSimple, Long> {
    Optional<CitaSimple> findById(Long id);
}