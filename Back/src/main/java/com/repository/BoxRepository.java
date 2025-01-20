package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findById(Long id);
    List<Box> findByEstadoTrue();
}
