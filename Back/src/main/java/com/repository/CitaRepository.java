package com.repository;

import com.models.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    Optional<Cita> findById(Long id);

   List<Cita> findByPacienteRut(String rut);
    List<Cita> findByProfesionalUsername(String username);
    
}
