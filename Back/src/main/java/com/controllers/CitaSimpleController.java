package com.controllers;

import com.models.CitaSimple;
import com.repository.CitaSimpleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/cita_simple")
public class CitaSimpleController {

    @Autowired
    private CitaSimpleRepository citaSimpleRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CitaSimple>> getAllCitasSimples() {
        List<CitaSimple> citasSimples = citaSimpleRepository.findAll();
        return new ResponseEntity<>(citasSimples, HttpStatus.OK);
    }

    @GetMapping("/{idCitaSimple}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CitaSimple> getCitaSimpleById(@PathVariable Long idCitaSimple) {
        Optional<CitaSimple> citaSimple = citaSimpleRepository.findById(idCitaSimple);
        return citaSimple.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CitaSimple> addCitaSimple(@RequestBody CitaSimple citaSimple) {
        CitaSimple newCitaSimple = citaSimpleRepository.save(citaSimple);
        return new ResponseEntity<>(newCitaSimple, HttpStatus.CREATED);
    }

    @PutMapping("/update/{idCitaSimple}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CitaSimple> updateCitaSimple(@PathVariable Long idCitaSimple, @RequestBody CitaSimple citaSimpleDetails) {
        Optional<CitaSimple> citaSimple = citaSimpleRepository.findById(idCitaSimple);
        if (citaSimple.isPresent()) {
            CitaSimple updatedCitaSimple = citaSimple.get();
            updatedCitaSimple.setTipoCita(citaSimpleDetails.getTipoCita());
            updatedCitaSimple.setPaciente(citaSimpleDetails.getPaciente());
            citaSimpleRepository.save(updatedCitaSimple);
            return new ResponseEntity<>(updatedCitaSimple, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{idCitaSimple}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteCitaSimple(@PathVariable Long idCitaSimple) {
        try {
            citaSimpleRepository.deleteById(idCitaSimple);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}