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
                    citaMap.put("profesional", cita.getProfesional().getUsername());
                    citaMap.put("sector", cita.getSector());
                    return citaMap;
                })
                .collect(Collectors.toList());

            response.put("paciente", pacienteInfo);
            response.put("citas", citasInfo);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Nuevo endpoint para listar citas por username del profesional
    @GetMapping("/profesional/buscar/{username}")

    public ResponseEntity<Map<String, Object>> buscarProfesional(@PathVariable String username) {
    try {
        List<Cita> citasProfesional = citaRepository.findByProfesional_Username(username);
        if (citasProfesional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Obtener información del profesional de la primera cita
        User profesional = (User) citasProfesional.get(0).getProfesional();
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> profesionalInfo = new HashMap<>();
        profesionalInfo.put("username", profesional.getUsername());
        profesionalInfo.put("email", ((com.models.User) profesional).getEmail());
        profesionalInfo.put("nombre", profesional.getName());
        profesionalInfo.put("sector", citasProfesional.get(0).getSector());

        // Mapear las citas del profesional
        List<Map<String, Object>> citasInfo = citasProfesional.stream()
            .map(cita -> {
                Map<String, Object> citaMap = new HashMap<>();
                citaMap.put("id", cita.getId());
                citaMap.put("fecha", cita.getFechaCita());
                citaMap.put("hora", cita.getHoraCita());
                citaMap.put("estado", cita.getEstado());
                citaMap.put("paciente", cita.getPaciente().getNombre() + " " + 
                           cita.getPaciente().getApellido());
                citaMap.put("rutPaciente", cita.getPaciente().getRut());
                citaMap.put("tipoAtencion", cita.getTipoAtencion());
                citaMap.put("sector", cita.getSector());
                return citaMap;
            })
            .collect(Collectors.toList());

        response.put("profesional", profesionalInfo);
        response.put("citas", citasInfo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
