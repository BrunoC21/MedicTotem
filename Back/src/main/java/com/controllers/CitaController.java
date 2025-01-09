package com.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.models.Cita;
import com.repository.CitaRepository;
import com.service.CitaService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/cita")
public class CitaController {

    private final CitaRepository cita;
    private final CitaService citaService;

    @Autowired
    public CitaController(CitaRepository cita, CitaService citaService) {
        this.cita = cita;
        this.citaService = citaService;
    }

    @GetMapping("/all")
    public List<Cita> listaTodosBox() {
        return cita.findAll();
    }

    @PostMapping("/cargar")
    public String cargarDatosDesdeExcel(@RequestParam("file") MultipartFile file) {
        try {
            citaService.cargarDatosDesdeExcel(file.getInputStream());
            return "Datos cargados exitosamente";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al cargar los datos";
        }
    }
}

