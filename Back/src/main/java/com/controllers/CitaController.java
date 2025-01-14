package com.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.apache.catalina.User;
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
import com.models.Paciente;
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
    @GetMapping("/paciente/buscar/{rut}")
    public ResponseEntity<Map<String, Object>> buscarPaciente(@PathVariable String rut) {
        try {
            Optional<Paciente> paciente = pacienteRepository.findByRut(rut);
            if (!paciente.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Map<String, Object> response = new HashMap<>();
            Map<String, Object> pacienteInfo = new HashMap<>();
            pacienteInfo.put("rut", paciente.get().getRut());
            pacienteInfo.put("nombre", paciente.get().getNombre());
            pacienteInfo.put("apellido", paciente.get().getApellido());
            pacienteInfo.put("sexo", paciente.get().getSexo());
            pacienteInfo.put("direccion", paciente.get().getDireccion());

            List<Cita> citasPaciente = citaRepository.findByPaciente_Rut(rut);
            List<Map<String, Object>> citasInfo = citasPaciente.stream()
                .map(cita -> {
                    Map<String, Object> citaMap = new HashMap<>();
                    citaMap.put("fecha", cita.getFechaCita());
                    citaMap.put("hora", cita.getHoraCita());
                    citaMap.put("estado", cita.getEstado());
                    citaMap.put("nombreMedico", cita.getProfesional().getNombre());
                    citaMap.put("especialidad", cita.getProfesional().getEspecialidad());
                    citaMap.put("sector", cita.getSector());
                    citaMap.put("box", cita.getBox());
                    citaMap.put("tipoAtencion", cita.getTipoAtencion());
                    return citaMap;
                })
                .collect(Collectors.toList());

            response.put("paciente", pacienteInfo);
            response.put("citas", citasInfo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Nuevo endpoint para listar citas por username del profesional
    @GetMapping("/profesional/buscar/{username}")
    public ResponseEntity<?> buscarProfesional(@PathVariable String username) {
        try {
        List<Cita> citasProfesional = citaRepository.findByProfesional_Username(username);
        if (citasProfesional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontraron citas para el profesional: " + username);
        }

        // Obtener información del profesional de la primera cita
        com.models.User profesional = citasProfesional.get(0).getProfesional();
        if (profesional == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró información del profesional");
        }

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> profesionalInfo = new HashMap<>();
        
        try {
            profesionalInfo.put("username", profesional.getUsername());
            profesionalInfo.put("email", profesional.getEmail());
            profesionalInfo.put("nombre", profesional.getNombre());
            profesionalInfo.put("sector", citasProfesional.get(0).getSector());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener datos del profesional: " + e.getMessage());
        }

        // Mapear las citas del profesional con validación
        List<Map<String, Object>> citasInfo = citasProfesional.stream()
            .filter(cita -> cita.getPaciente() != null)
            .map(cita -> {
                Map<String, Object> citaMap = new HashMap<>();
                try {
                    citaMap.put("id", cita.getId());
                    citaMap.put("fecha", cita.getFechaCita());
                    citaMap.put("hora", cita.getHoraCita());
                    citaMap.put("estado", cita.getEstado());
                    citaMap.put("paciente", cita.getPaciente().getNombre() + " " + 
                               cita.getPaciente().getApellido());
                    citaMap.put("rutPaciente", cita.getPaciente().getRut());
                    citaMap.put("tipoAtencion", cita.getTipoAtencion());
                    citaMap.put("sector", cita.getSector());
                } catch (Exception e) {
                    System.out.println("Error al mapear cita: " + e.getMessage());
                }
                return citaMap;
            })
            .collect(Collectors.toList());

        response.put("profesional", profesionalInfo);
        response.put("citas", citasInfo);
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error del servidor: " + e.getMessage());
    }
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
