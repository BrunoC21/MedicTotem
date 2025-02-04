package com.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.Totem;
import com.repository.TotemRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/totem")
public class TotemController {

    @Autowired
    private TotemRepository totemRepository;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Totem>> getAllTotems() {
        List<Totem> totems = totemRepository.findAll();
        return new ResponseEntity<>(totems, HttpStatus.OK);
    }

    @GetMapping("/nroTicket/{sector}")
    public ResponseEntity<Integer> getNroTicket(@PathVariable String sector) {
        Optional<Totem> totemRepo = totemRepository.findBySector("Sector " + sector);
        
        if(totemRepo.isPresent()){
            Totem totem = totemRepo.get();
            int nroTicket = totem.getNroTicket();
            totem.countNroTicket();
            totemRepository.save(totem);
            return ResponseEntity.ok(nroTicket);
        }
        
        return ResponseEntity.notFound().build();
    }

    /*Funciones CRUD, no se estan utilizando actualmente 
    pero quedan disponibles por si se quiere usar para algo visual

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Totem> getTotemById(@PathVariable Long id) {
        Optional<Totem> totem = totemRepository.findById(id);
        return totem.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Totem> addTotem(@RequestBody Totem totem) {
        Totem newTotem = totemRepository.save(totem);
        return new ResponseEntity<>(newTotem, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Totem> updateTotem(@PathVariable Long id, @RequestBody Totem totemDetails) {
        Optional<Totem> totem = totemRepository.findById(id);
        if (totem.isPresent()) {
            Totem updatedTotem = totem.get();
            updatedTotem.setSector(totemDetails.getSector());
            totemRepository.save(updatedTotem);
            return new ResponseEntity<>(updatedTotem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTotem(@PathVariable Long id) {
        try {
            totemRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
        */
}