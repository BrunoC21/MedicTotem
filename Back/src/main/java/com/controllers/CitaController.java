package com.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.models.Cita;
import com.repository.CitaRepository;
import com.security.services.UserDetailsImpl;
import com.service.ExcelService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/cita")
public class CitaController {

    private final CitaRepository citaRepository;
    private final ExcelService excelService;

    public CitaController(CitaRepository citaRepository, ExcelService excelService) {
        this.citaRepository = citaRepository;
        this.excelService = excelService;
    }

    //Obtener todas las citas
    @GetMapping("/all")
    public List<Cita> listaTodosBox() {
        return citaRepository.findAll();
    }

    //Obtener una cita por su rut del paciente
    @GetMapping("/paciente/{rutPaciente}")
    public ResponseEntity<List<Cita>> getCitasByPaciente(@PathVariable String rutPaciente) {
        List<Cita> citasPaciente = citaRepository.findByPacienteRut(rutPaciente);
        return ResponseEntity.ok(citasPaciente);
    }

    //Obtener una cita por su rut del paciente y sector de la cita
    @GetMapping("/paciente/{rut}/{sector}")
    public List<Cita> obtenerCitasPorRutPaciente(@PathVariable String rut, @PathVariable String sector) {
        return citaRepository.findByPacienteRutAndSector(rut, "Sector " + sector);
    }

    //Actualiza el estado de la cita
    @PutMapping("/actualizarEstado/{id}")
    public String actualizarEstadoCita(@PathVariable Long id) {
        Optional<Cita> citaOptional = citaRepository.findById(id);
        if (citaOptional.isPresent()) {
            Cita cita = citaOptional.get();
            cita.setEstado("Confirmado");
            citaRepository.save(cita);
            return "Estado de la cita actualizado exitosamente";
        } else {
            return "Cita no encontrada";
        }
    }

    //Actualiza el estado de la llamada
    @PutMapping("/actualizarEstadoLlamada/{id}")
    public String actualizarEstadoLlamada(@PathVariable Long id) {
        Optional<Cita> citaOptional = citaRepository.findById(id);
        if (citaOptional.isPresent()) {
            Cita cita = citaOptional.get();
            cita.setEstadoLlamado(true);
            citaRepository.save(cita);
            return "Estado de la cita actualizado exitosamente";
        } else {
            return "Cita no encontrada";
        }
    }

    //actualizar estado de termino
    @PutMapping("/actualizarEstadoTermino/{id}")
    public String actualizarEstadoTermino(@PathVariable Long id) {
        Optional<Cita> citaOptional = citaRepository.findById(id);
        if (citaOptional.isPresent()) {
            Cita cita = citaOptional.get();
            cita.setEstadoTermino(true);
            citaRepository.save(cita);
            return "Estado de la cita actualizado exitosamente";
        } else {
            return "Cita no encontrada";
        }
    }

    //Obtener cita asignadas por id del profecional
    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<List<Cita>> getCitasByProfesional(@PathVariable Long profesionalId) {
        List<Cita> citasProfesional = citaRepository.findByProfesionalId(profesionalId);
        return ResponseEntity.ok(citasProfesional);
    }

    // Obtener citas asignadas al profesional logueado
    @GetMapping("/profesional")
    public List<Cita> getCitasByProfesionalLogueado(Authentication authentication) {
        Long profesionalId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        List<Cita> citas = citaRepository.findByProfesionalId(profesionalId);
        return citas.stream()
                .filter(cita -> cita.getEstadoTermino() == null || !cita.getEstadoTermino())
                .filter(cita -> cita.getEstado().equalsIgnoreCase("Confirmado"))
                .collect(Collectors.toList());
    }

    //Cargar datos del excel
    @PostMapping("/cargar")
    public String cargarDatosDesdeExcel(@RequestParam("file") MultipartFile file) {
        try {
            excelService.cargarDatosDesdeExcel(file.getInputStream());
            return "Datos cargados exitosamente";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al cargar los datos";
        }
    }
}

