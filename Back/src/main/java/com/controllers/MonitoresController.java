package com.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.Ticket;
import com.repository.TicketRepository;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/monitores")
public class MonitoresController {

    @Autowired  
    private final TicketRepository ticketRepository;

    public MonitoresController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/all")
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    //Obtener todos los tickets
    @GetMapping("/Sector1")
    public List<Ticket> listaTodosTicketsSector1() {
        return ticketRepository.findByTotemSector("Sector 1").stream()
                .filter(ticket -> "Llamado".equals(ticket.getEstado()))
                .collect(Collectors.toList());
    }

    @GetMapping("/Sector2")
    public List<Ticket> listaTodosTickets2() {
        return ticketRepository.findByTotemSector("Sector 2").stream()
        .filter(ticket -> "Llamado".equals(ticket.getEstado()))
        .collect(Collectors.toList());
    }

    @GetMapping("/Sector4")
    public List<Ticket> listaTodosTickets4() {
        return ticketRepository.findByTotemSector("Sector 4").stream()
        .filter(ticket -> "Llamado".equals(ticket.getEstado()))
        .collect(Collectors.toList());
    }

    @GetMapping("/Sector5")
    public List<Ticket> listaTodosTickets5() {
        return ticketRepository.findByTotemSector("Sector 5").stream()
        .filter(ticket -> "Llamado".equals(ticket.getEstado()))
        .collect(Collectors.toList());
    }

    @GetMapping("/SectorDental")
    public List<Ticket> listaTodosTicketsDental() {
        return ticketRepository.findByTotemSector("Sector Dental").stream()
        .filter(ticket -> "Llamado".equals(ticket.getEstado()))
        .collect(Collectors.toList());
    }
    
}
