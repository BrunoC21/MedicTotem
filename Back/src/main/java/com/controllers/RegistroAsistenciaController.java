package com.controllers;

import com.models.RegistroAsistencia;
import com.repository.RegistroAsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/registro_asistencia")
public class RegistroAsistenciaController {

    @Autowired
    private RegistroAsistenciaRepository registroAsistenciaRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RegistroAsistencia>> getAllRegistrosAsistencia() {
        List<RegistroAsistencia> registrosAsistencia = registroAsistenciaRepository.findAll();
        return new ResponseEntity<>(registrosAsistencia, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegistroAsistencia> getRegistroAsistenciaById(@PathVariable Long id) {
        Optional<RegistroAsistencia> registroAsistencia = registroAsistenciaRepository.findById(id);
        return registroAsistencia.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegistroAsistencia> addRegistroAsistencia(@RequestBody RegistroAsistencia registroAsistencia) {
        RegistroAsistencia newRegistroAsistencia = registroAsistenciaRepository.save(registroAsistencia);
        return new ResponseEntity<>(newRegistroAsistencia, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegistroAsistencia> updateRegistroAsistencia(@PathVariable Long id, @RequestBody RegistroAsistencia registroAsistenciaDetails) {
        Optional<RegistroAsistencia> registroAsistencia = registroAsistenciaRepository.findById(id);
        if (registroAsistencia.isPresent()) {
            RegistroAsistencia updatedRegistroAsistencia = registroAsistencia.get();
            updatedRegistroAsistencia.setFechaFinalizacion(registroAsistenciaDetails.getFechaFinalizacion());
            updatedRegistroAsistencia.setTiempoEspera(registroAsistenciaDetails.getTiempoEspera());
            updatedRegistroAsistencia.setEstado(registroAsistenciaDetails.getEstado());
            updatedRegistroAsistencia.setObservacion(registroAsistenciaDetails.getObservacion());
            registroAsistenciaRepository.save(updatedRegistroAsistencia);
            return new ResponseEntity<>(updatedRegistroAsistencia, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteRegistroAsistencia(@PathVariable Long id) {
        try {
            registroAsistenciaRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}