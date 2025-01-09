package com.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.Cita;
import com.repository.CitaRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/cita")
public class CitaController {
    
    private CitaRepository cita;

    public CitaController(CitaRepository cita) {
        this.cita = cita;
    }

    @GetMapping("/all")
    public List<Cita> listaTodasCitas(){
        return cita.findAll();
    }
}
