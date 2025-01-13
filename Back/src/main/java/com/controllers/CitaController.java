package com.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.models.Cita;
import com.models.Paciente;
import com.models.User;
import com.repository.CitaRepository;
import com.repository.PacienteRepository;

@RestController
@RequestMapping("/api/cita")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CitaController {
    
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
        }
    }
}
