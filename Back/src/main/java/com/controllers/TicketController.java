package com.controllers;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
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

import com.models.Cita;
import com.models.Ticket;
import com.models.Totem;
import com.repository.CitaRepository;
import com.repository.TicketRepository;
import com.repository.TotemRepository;


@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private TotemRepository totemRepository;

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

    @GetMapping("/estado/{id}")
    public ResponseEntity<Ticket> getTicketByCita(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findByCitaId(id);
        return ticket.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create/{id}")
    public ResponseEntity<?> createTicket(@PathVariable Long id) {
        
        Optional<Cita> citaOptional = citaRepository.findById(id);
        if (citaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("message", "Cita no encontrada para ID: " + id));
        }

        Cita cita = citaOptional.get();
        String sector = cita.getSector();

        if(sector.equals("Sector Transversal")){
            sector = asignarTransversal(cita.getTipoAtencion());
        }

        Optional<Totem> totemOptional = totemRepository.findBySector(sector);
        if (totemOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("message", "No hay totem disponible para el sector: " + sector));
        }

        ZoneId zonaChile = ZoneId.of("America/Santiago");
        ZonedDateTime ahora = ZonedDateTime.now(zonaChile);

        Ticket newTicket = new Ticket();
        newTicket.setCita(cita);
        newTicket.setTotem(totemOptional.get());
        newTicket.setEstado("Pendiente");
        newTicket.setHora_confirmacion(ahora.toLocalTime().truncatedTo(ChronoUnit.SECONDS));
        newTicket.setFecha(ahora.toLocalDate());

        Ticket savedTicket = ticketRepository.save(newTicket);

        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(Map.of("message", "Ticket creado exitosamente", 
                                        "ticketId", savedTicket.getId()));
    }


    /* Funcion que se llama para poder optimisar el asiganado de totem de cada ticket
    En caso de que la cita pertenesca al sector transversal */
    public String asignarTransversal(String tipoAtencion){
        String atencion = tipoAtencion.toLowerCase();

        if (atencion.contains("dental")) {
            return "Sector Dental";
        } 
        if (atencion.contains("discapacidad")) {
            return "Sector 4";
        } 
        if (atencion.contains("era")) {
            return "Sector 2";
        } 

        return switch (tipoAtencion) {
            case "Ecografía Obstétrica con doppler" -> "Sector 2";
            case "Control Pie Diabético" -> "Sector 2";
            case "Curaciones" -> "Sector 4";
            case "Sector Transversal" -> "Sector 5";
            default -> "Sector 1";
        };
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ticket> updateTicket(@PathVariable Long id, @RequestBody Ticket ticketDetails) {
        
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            Ticket updatedTicket = ticket.get();
            updatedTicket.setEstado(ticketDetails.getEstado());
            updatedTicket.setHora_confirmacion(ticketDetails.getHora_confirmacion().truncatedTo(ChronoUnit.SECONDS));
            updatedTicket.setHora_llamada(ticketDetails.getHora_llamada().truncatedTo(ChronoUnit.SECONDS));
            updatedTicket.setHora_termino(ticketDetails.getHora_termino().truncatedTo(ChronoUnit.SECONDS));
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
            updatedTicket.setHora_confirmacion(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
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
            updatedTicket.setHora_llamada(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
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
            updatedTicket.setHora_termino(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
            ticketRepository.save(updatedTicket);
            return new ResponseEntity<>(updatedTicket, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateEstado/{id}")
    public ResponseEntity<?> updateEstado(@PathVariable Long id, @RequestBody String estado) {
        if (!isValidEstado(estado)) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Estado inválido. Use: PENDIENTE, TERMINADO o PERDIDO"));
        }

        return ticketRepository.findByCitaId(id)
            .map(ticket -> {
                ticket.setEstado(estado);
                ticketRepository.save(ticket);
                return ResponseEntity.ok()
                    .body(Map.of("message", "Estado actualizado exitosamente", 
                                "ticket", ticket));
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Ticket no encontrado con ID: " + id)));
    }

    private boolean isValidEstado(String estado) {
        return estado != null && (
            estado.equals("Terminado") ||
            estado.equals("Preparado") ||
            estado.equals("Llamado") ||
            estado.equals("Perdido") 
        );
    }

    @GetMapping("/cita/{rut}")
    public ResponseEntity<?> getTicketsByCitaRut(@PathVariable String rut) {
        Optional<Ticket> ticketOptional = ticketRepository.findByCitaPacienteRut(rut);
        if (ticketOptional.isPresent()) {
            Long ticketId = ticketOptional.get().getId();
            return ResponseEntity.ok(ticketId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Map.of("error", "Ticket no encontrado para el rut: " + rut));
        }
    }
}