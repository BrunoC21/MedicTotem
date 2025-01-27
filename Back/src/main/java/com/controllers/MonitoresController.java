package com.controllers;

import java.util.List;

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
    public List<Ticket> listaTodosTickets() {
        return ticketRepository.findByTotemSector("Sector 1");
    }

    @GetMapping("/Sector2")
    public List<Ticket> listaTodosTickets2() {
        return ticketRepository.findByTotemSector("Sector 2");
    }

    @GetMapping("/Sector4")
    public List<Ticket> listaTodosTickets4() {
        return ticketRepository.findByTotemSector("Sector 4");
    }

    @GetMapping("/Sector5")
    public List<Ticket> listaTodosTickets5() {
        return ticketRepository.findByTotemSector("Sector 5");
    }
    
}
