package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    @SuppressWarnings("null")
    Optional<Paciente> findById(Long id);

    Paciente findByRut(String rut);

}