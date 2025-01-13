package com.controllers;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
=======
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
>>>>>>> 20e881b3f6c3537d967fa4e2dc85b719f51af781

import com.models.Cita;
import com.models.Paciente;
import com.models.User;
import com.repository.CitaRepository;
<<<<<<< HEAD
import com.repository.PacienteRepository;

=======
import com.service.CitaService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
>>>>>>> 20e881b3f6c3537d967fa4e2dc85b719f51af781
@RestController
@RequestMapping("/api/cita")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CitaController {
<<<<<<< HEAD
    
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/all")
    
    public ResponseEntity<List<Map<String, Object>>> getAllCitas() {
        try {
            List<Cita> citas = citaRepository.findAll();
            List<Map<String, Object>> citasResponse = citas.stream()
                .map(cita -> {
                    Map<String, Object> citaMap = new HashMap<>();
                    citaMap.put("id", cita.getId());
                    citaMap.put("fecha", cita.getFechaCita());
                    citaMap.put("hora", cita.getHoraCita());
                    citaMap.put("estado", cita.getEstado());
                    citaMap.put("tipoAtencion", cita.getTipoAtencion());
                    citaMap.put("sector", cita.getSector());
                    citaMap.put("profesional", cita.getProfesional().getUsername());
                    citaMap.put("paciente", cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido());
                    citaMap.put("rutPaciente", cita.getPaciente().getRut());
                    return citaMap;
                })
                .collect(Collectors.toList());
            return new ResponseEntity<>(citasResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/paciente/{rut}")
  
    public ResponseEntity<Map<String, Object>> getCitasPaciente(@PathVariable String rut) {
        try {
            Optional<Paciente> paciente = pacienteRepository.findByRut(rut);
            if (!paciente.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            List<Cita> citas = citaRepository.findByPacienteRut(rut);
            Map<String, Object> response = new HashMap<>();
            
            Map<String, Object> pacienteInfo = new HashMap<>();
            pacienteInfo.put("nombre", paciente.get().getNombre());
            pacienteInfo.put("apellido", paciente.get().getApellido());
            pacienteInfo.put("rut", paciente.get().getRut());

            List<Map<String, Object>> citasList = citas.stream()
                .map(cita -> {
                    Map<String, Object> citaMap = new HashMap<>();
                    citaMap.put("tipoCita", cita.getTipoAtencion());
                    citaMap.put("fecha", cita.getFechaCita());
                    citaMap.put("hora", cita.getHoraCita());
                    citaMap.put("profesional", cita.getProfesional().getUsername());
                    citaMap.put("estado", cita.getEstado());
                    return citaMap;
                })
                .collect(Collectors.toList());

            response.put("paciente", pacienteInfo);
            response.put("citas", citasList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profesional/{username}")
   
    public ResponseEntity<Map<String, Object>> getCitasProfesional(@PathVariable String username) {
        try {
            List<Cita> citas = citaRepository.findByProfesionalUsername(username);
            if (citas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            User profesional = citas.get(0).getProfesional();
            Map<String, Object> response = new HashMap<>();
            
            Map<String, Object> profesionalInfo = new HashMap<>();
            profesionalInfo.put("username", profesional.getUsername());
            profesionalInfo.put("box", profesional.getBox() != null ? profesional.getBox().getId() : null);

            List<Map<String, Object>> citasPendientes = citas.stream()
                .filter(cita -> "PENDIENTE".equals(cita.getEstado()))
                .map(cita -> {
                    Map<String, Object> citaMap = new HashMap<>();
                    citaMap.put("id", cita.getId());
                    citaMap.put("fecha", cita.getFechaCita());
                    citaMap.put("hora", cita.getHoraCita());
                    citaMap.put("sector", cita.getSector());
                    citaMap.put("paciente", cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido());
                    citaMap.put("rutPaciente", cita.getPaciente().getRut());
                    return citaMap;
                })
                .collect(Collectors.toList());

            response.put("profesional", profesionalInfo);
            response.put("citasPendientes", citasPendientes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
=======

    private final CitaRepository cita;
    private final CitaService citaService;

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
>>>>>>> 20e881b3f6c3537d967fa4e2dc85b719f51af781
        }
    }
}

