package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.models.Ticket;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @SuppressWarnings("null")
    Optional<Ticket> findById(Long id);
    Optional <Ticket> findByCitaId(Long id);
    Optional <Ticket> findByCitaPacienteRut(String rut);

    List<Ticket> findByTotemSector(String sector);
}