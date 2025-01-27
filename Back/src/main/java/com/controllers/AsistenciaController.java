package com.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.AsistenciaMedica;
import com.models.User;
import com.repository.AsistenciaRepository;
import com.repository.UserRepository;
import com.security.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {

    private AsistenciaRepository asistenciaRepository;
    private UserRepository userRepository;

    public AsistenciaController(AsistenciaRepository asistenciaRepository, UserRepository userRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<AsistenciaMedica> listaTodasAsistencias() {
        return asistenciaRepository.findAll();
    }

    @PostMapping("/new")
    public ResponseEntity<String> crearFichaAsistencia(Authentication authentication) {
        Long profesionalId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        Optional<User> userOptional = userRepository.findById(profesionalId);

        if (userOptional.isPresent()) {
            User profesional = userOptional.get();

            AsistenciaMedica asistencia = new AsistenciaMedica();
            asistencia.setFecha(LocalDate.now());
            asistencia.setHoraInicio(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
            asistencia.setProfesional(profesional);

            asistenciaRepository.save(asistencia);
            return ResponseEntity.ok("Ficha de asistencia creada exitosamente");
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    @PutMapping("/updateFechaTermino")
    public ResponseEntity<String> actualizarFechaTermino(Authentication authentication) {
        Long profesionalId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        
        List<AsistenciaMedica> asistencia = asistenciaRepository.findByProfesionalId(profesionalId);
        
        AsistenciaMedica asistenciaReciente = asistencia.stream()
                .max(Comparator.comparing(AsistenciaMedica::getHoraInicio))
                .orElse(null);

                asistenciaReciente.setHoraTermino(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
        asistenciaRepository.save(asistenciaReciente);
        return ResponseEntity.ok("Fecha de término actualizada exitosamente");
    }


}
