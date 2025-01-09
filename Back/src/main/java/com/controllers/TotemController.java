package com.controllers;

import com.models.Totem;
import com.repository.TotemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
}