package com.controllers;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.Ticket;
import com.repository.TicketRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    private int ticketNumber = 1; // Puedes persistir esto en la base de datos.

    @GetMapping("/next")
    public ResponseEntity<Integer> getNextTicketNumber() {
        int currentTicketNumber = ticketNumber++;
        return ResponseEntity.ok(currentTicketNumber);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket) {
        Ticket newTicket = ticketRepository.save(ticket);
        return new ResponseEntity<>(newTicket, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticketDetails) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            Ticket updatedTicket = ticket.get();
            updatedTicket.setEstado(ticketDetails.getEstado());
            updatedTicket.setHora_confirmacion(ticketDetails.getHora_confirmacion());
            updatedTicket.setHora_llamada(ticketDetails.getHora_llamada());
            updatedTicket.setHora_termino(ticketDetails.getHora_termino());
            updatedTicket.setFecha(ticketDetails.getFecha());
            ticketRepository.save(updatedTicket);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTicket(@PathVariable Long id) {
        try {
            ticketRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateHoraConfirmacion/{id}")
    public ResponseEntity<Ticket> updateHoraConfirmacion(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            Ticket updatedTicket = ticket.get();
            updatedTicket.setHora_confirmacion(LocalTime.now());
            ticketRepository.save(updatedTicket);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateHoraLlamada/{id}")
    public ResponseEntity<Ticket> updateHoraLlamada(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findByCitaId(id);
        if (ticket.isPresent()) {
            Ticket updatedTicket = ticket.get();
            updatedTicket.setHora_llamada(LocalTime.now());
            ticketRepository.save(updatedTicket);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateHoraTermino/{id}")
    public ResponseEntity<Ticket> updateHoraTermino(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findByCitaId(id);
        if (ticket.isPresent()) {
            Ticket updatedTicket = ticket.get();
            updatedTicket.setHora_termino(LocalTime.now());
            ticketRepository.save(updatedTicket);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}