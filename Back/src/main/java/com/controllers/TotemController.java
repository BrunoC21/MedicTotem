package com.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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

    /*Esta funcion permite obtener y aumentar el numero de ticket para cada 
    sector */
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
    

    /*Esta funcion es para reiniciar el nroTicket todos los dias
    a las 12 de la noche, se ejecuta automanticamente*/
    @Scheduled(cron = "0 0 0 * * ?") 
    public void resetNroTickets() {
        List<Totem> totems = totemRepository.findAll();
        for (Totem totem : totems) {
            totem.resetNroTicket();
            totemRepository.save(totem);
        }
    }
}