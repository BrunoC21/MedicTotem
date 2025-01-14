package com.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.models.Cita;
import com.repository.CitaRepository;
import com.repository.PacienteRepository;
import com.service.CitaService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/cita")
public class CitaController {

    private final CitaRepository citaRepository;
    private final CitaService citaService;
    private final PacienteRepository pacienteRepository;

    public CitaController(CitaRepository citaRepository, CitaService citaService, 
                         PacienteRepository pacienteRepository) {
        this.citaRepository = citaRepository;
        this.citaService = citaService;
        this.pacienteRepository = pacienteRepository;
    }

    // Endpoint existente
    @GetMapping("/all")
    public List<Cita> listaTodosCitas() {
        return citaRepository.findAll();
    }

    // Nuevo endpoint para listar citas por rut de paciente
    @GetMapping("/paciente/{rut}")
    public ResponseEntity<?> getCitasByPacienteRut(@PathVariable String rut) {
        List<Cita> citas = citaRepository.findByPaciente_Rut(rut);
        if (citas.isEmpty()) {
            return new ResponseEntity<>(
                "No se encontraron citas para el paciente con RUT: " + rut, 
                HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> citasFormateadas = citas.stream()
            .map(cita -> {
                Map<String, Object> citaMap = new HashMap<>();
                citaMap.put("fecha", cita.getFechaCita());
                citaMap.put("hora", cita.getHoraCita());
                citaMap.put("nombreMedico", cita.getProfesional().getNombre());
                citaMap.put("tipoCita", cita.getTipoAtencion());
                citaMap.put("box", cita.getId());
                citaMap.put("nombrePaciente", cita.getPaciente().getNombre());
                citaMap.put("apellidoPaciente", cita.getPaciente().getApellido());
                citaMap.put("estado", cita.getEstado());
                return citaMap;
            })
            .collect(Collectors.toList());

        if (!citasFormateadas.isEmpty()) {
            Map<String, Object> primeraCita = citasFormateadas.get(0);
            response.put("nombrePaciente", primeraCita.get("nombrePaciente"));
            response.put("apellidoPaciente", primeraCita.get("apellidoPaciente"));
            response.put("citas", citasFormateadas);
        }

        return ResponseEntity.ok(response);
    }

    // Nuevo endpoint para listar citas por username del profesional
    @GetMapping("/profesional/{username}")
    public ResponseEntity<?> getCitasByProfesionalUsername(@PathVariable String username) {
        List<Cita> citas = citaRepository.findByProfesional_Username(username);
        if (citas.isEmpty()) {
            return new ResponseEntity<>(
                "No se encontraron citas para el profesional: " + username, 
                HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> citasFormateadas = citas.stream()
            .map(cita -> {
                Map<String, Object> citaMap = new HashMap<>();
                citaMap.put("nombreMedico", cita.getProfesional().getNombre());
                citaMap.put("fecha", cita.getFechaCita());
                citaMap.put("hora", cita.getHoraCita());
                citaMap.put("pacienteNombre", cita.getPaciente().getNombre());
                citaMap.put("pacienteRut", cita.getPaciente().getRut());
                citaMap.put("estado", cita.getEstado());
                return citaMap;
            })
            .collect(Collectors.toList());

        if (!citasFormateadas.isEmpty()) {
            response.put("nombreMedico", citasFormateadas.get(0).get("nombreMedico"));
            response.put("especialidad", citasFormateadas.get(0).get("especialidad"));
            response.put("citas", citasFormateadas);
        }

        return ResponseEntity.ok(response);
    }

    // Endpoint existente para cargar datos
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
