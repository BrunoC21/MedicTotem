package com.service.CitasSimples;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaSimpleRepository extends JpaRepository<CitaSimple, Long> {
    @SuppressWarnings("null")
    Optional<CitaSimple> findById(Long id);
    Optional<CitaSimple> findByTipoCita(String tipoCita);
}