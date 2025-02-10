package com.service.CitasSimples;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/citaSimple")
public class CitaSimpleController {

    @Autowired
    private CitaSimpleRepository citaSimpleRepository;

    @GetMapping("/all") 
    public ResponseEntity<List<CitaSimple>> getCitasSimple() {
        List<CitaSimple> CitaSimples = citaSimpleRepository.findAll();
        return new ResponseEntity<>(CitaSimples, HttpStatus.OK);
    }

    /*Esta funcion permite obtener y aumentar el numero de ticket para cada 
    sector */
    @GetMapping("/nroTicket/{tipoCita}")
    public ResponseEntity<Integer> getNroTicket(@PathVariable String tipoCita) {
        Optional<CitaSimple> citaSimple = citaSimpleRepository.findByTipoCita(tipoCita);
        
        if(citaSimple.isPresent()){
            CitaSimple cita = citaSimple.get();
            int nroTicket = cita.getNroTicket();
            cita.countNroTicket();
            citaSimpleRepository.save(cita);
            return ResponseEntity.ok(nroTicket);
        }
         
        return ResponseEntity.notFound().build();
    }
    

    /*Esta funcion es para reiniciar el nroTicket todos los dias
    a las 12 de la noche, se ejecuta automanticamente*/
    @Scheduled(cron = "0 0 0 * * ?") 
    public void resetNroTickets() {
        List<CitaSimple> CitaSimples = citaSimpleRepository.findAll();
        for (CitaSimple cita : CitaSimples) {
            cita.resetNroTicket();
            citaSimpleRepository.save(cita);
        }
    }



}