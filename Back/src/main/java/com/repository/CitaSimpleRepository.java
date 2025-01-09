package com.repository;

import com.models.CitaSimple;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaSimpleRepository extends JpaRepository<CitaSimple, Long> {
    Optional<CitaSimple> findById(Long id);
}