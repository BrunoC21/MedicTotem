package com.controllers;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.Ticket;
import com.models.User;
import com.models.registroTens;
import com.repository.TicketRepository;
import com.repository.UserRepository;
import com.repository.registroTensRepository;
import com.security.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/tens")
public class registroTensController {
    
    @Autowired
    private registroTensRepository registroTensRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping("/crear/{ticketId}")
    public ResponseEntity<?> crearRegistro(@PathVariable Long ticketId, Authentication authentication) {
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findById(userDetails.getId());
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));

        // Crear nuevo registro
        User rellenarUser = user.get();

        registroTens registro = new registroTens();
        registro.setTicket(ticket);
        registro.setNombre(rellenarUser.getNombre() + " " + rellenarUser.getApellido());
        registro.setRut(rellenarUser.getRut());
        registro.setHoraInicio(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));

        registroTensRepository.save(registro);

        return ResponseEntity.ok()
            .body(Map.of("mensaje", "Registro creado exitosamente", 
                        "registroId", registro.getId()));
    }

    @PutMapping("/terminar")
    public ResponseEntity<?> terminarRegistro(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = userRepository.findById(userDetails.getId());
        
        // Buscar registro por rut del usuario
        Optional<registroTens> registro = registroTensRepository.findByRut(user.get().getRut());
    
        // Actualizar hora de término
        registroTens registroActualizar = registro.get();
        registroActualizar.setHoraTermino(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        
        registroTensRepository.save(registroActualizar);

        return ResponseEntity.ok()
            .body(Map.of("mensaje", "Registro actualizado exitosamente",
                        "registroId", registroActualizar.getId()));
    }


}
