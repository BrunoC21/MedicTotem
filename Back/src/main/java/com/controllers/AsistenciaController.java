package com.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.AsistenciaMedica;
import com.repository.AsistenciaRepository;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {

    private AsistenciaRepository asistenciaRepository;

    public AsistenciaController(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    @GetMapping("/all")
    public List<AsistenciaMedica> listaTodasAsistencias(){
        return asistenciaRepository.findAll();
    }

    
}
