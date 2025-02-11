package com.controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.models.Cita;
import com.repository.CitaRepository;
import com.security.services.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/cita")
public class CitaController {

    private final CitaRepository citaRepository;

    public CitaController(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    //Obtener todas las citas
    @GetMapping("/all")
    public List<Cita> listaTodosBox() {
        return citaRepository.findAll();
    }

    //Obtener una cita por su rut del paciente
    /*Implementado pero no ocupado en el front */
    @GetMapping("/paciente/{rutPaciente}")
    public ResponseEntity<List<Cita>> getCitasByPaciente(@PathVariable String rutPaciente) {
        List<Cita> citasPaciente = citaRepository.findByPacienteRut(rutPaciente);
        return ResponseEntity.ok(citasPaciente);
    }

    //Obtener una cita por su rut del paciente y sector de la cita
    /*Este se ocupa para obtener las citas de los sectores principales 
    siendo, Sector 1, 2, 4, 5 y Dental*/
    @GetMapping("/paciente/{rut}/{sector}")
    public ResponseEntity<List<Cita>> obtenerCitasPorRutPaciente(@PathVariable String rut, @PathVariable String sector) {
        List<Cita> citas = citaRepository.findByPacienteRutAndSector(rut, "Sector " + sector);
    
        LocalDate hoy = LocalDate.now();
        
        List<Cita> citasHoy = citas.stream()
            .filter(cita -> cita.getFechaCita().equals(hoy))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(citasHoy);
    }

    //Obtener una cita por su rut del paciente y tipo de atencion
    /*Esto se ocupa para poder obtener las citas de los sectores transversales
    filtrando entonces por el tipo de atencion, en vez de por sector. 
    Los tipos de atencion se defininen en el ScreenDate en el que se atiende cada sector Transversal*/
    @GetMapping("/tipos/{rut}")
    public ResponseEntity<List<Cita>> obtenerCitasPorRutYTipos(@RequestParam String rut, @RequestParam List<String> tipos) {
        List<Cita> citas = citaRepository.findByPacienteRutAndTipoAtencionIn(rut, tipos);

        LocalDate hoy = LocalDate.now();
        
        List<Cita> citasHoy = citas.stream()
            .filter(cita -> cita.getFechaCita().equals(hoy))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(citasHoy);
    }

    //Actualiza el estado de la cita
    /*Actualiza el estado de la cita, para pasar a confirmado, momento en el que es entregado
    a los medicos. En caso de las citas con motivo medico, primero pasan a preparacion para 
    poder ser llamadas por las Tens, por lo que en caso de pertenecer a uno de los tipo de citas 
    relacionados a motico medico el estado pasa a pendiente, siendo entregada a las Tensa*/
    @PutMapping("/actualizarEstado/{id}")
    public String actualizarEstadoCita(@PathVariable Long id) {
        Optional<Cita> citaOptional = citaRepository.findById(id);
        if (citaOptional.isPresent()) {
            String tipoAtencion = citaOptional.get().getTipoAtencion();
            if (tipoAtencion.equals("control cardiovascular") || 
                tipoAtencion.equals("Otras Morbilidades") || 
                tipoAtencion.equals("Morbilidad Adulto") || 
                tipoAtencion.equals("Examenes de Morbilidad")) {
                
                Cita cita = citaOptional.get();
                cita.setEstado("Preparacion");
                citaRepository.save(cita);
                return "Estado de la cita actualizado exitosamente";
            } else {
                Cita cita = citaOptional.get();
                cita.setEstado("Confirmado");
                citaRepository.save(cita);
                return "Estado de la cita actualizado exitosamente";
            }
        } else {
            return "Cita no encontrada";
        }
    }

    //Actualizar estado luego de preparacion
    /*Como se dijo antes, si la cita es con motivo medico, pasa a estado pendiente
    lo que significa que pasa a preparacion(Con las tens), lo que hace esta funcion es 
    devolver la cita a manos del medico, dandole ahora si, el valor de confirmado*/
    @PutMapping("/actualizarEstadoTerminoTens/{id}")
    public String actualizarEstadoTerminoTens(@PathVariable Long id) {
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
    /*Actualiza el atributo EstadoLlamada para que pase a "True" o 1,
    la idea de este valor, es poder indicarle al sistema, cuando debe aparecer en
    la pantalla de llamada(Screendate)*/
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
    /*Actualiza el atributo EstadoTermino para que pase a "True" o 1,
    la idea de este valor, es poder indicarle al sistema, cuando debe desaparecer en
    la pantalla de llamada(Screendate) la cita. Ademas de dejar de aparecer dentro
    del sistema*/
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

    // Obtener citas asignadas al medico logueado
    /*Obtiene las citas asociadas al medico logueado, esto con el objetivo 
     de poder mostrarlas en la pantalla perteneciente.*/
    @GetMapping("/profesional")
    public List<Cita> getCitasByProfesionalLogueado(Authentication authentication) {
        Long profesionalId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        List<Cita> citas = citaRepository.findByProfesionalId(profesionalId);
        return citas.stream()
            .filter(cita -> cita.getEstadoTermino() == null || !cita.getEstadoTermino())
            .filter(cita -> cita.getEstado().equalsIgnoreCase("Confirmado"))
            .collect(Collectors.toList());
    }

    // Obtener citas asignadas a la Tens logueado
    /*Obtiene las citas asociadas al medico logueado, esto con el objetivo 
     de poder mostrarlas en la pantalla perteneciente.*/
    @GetMapping("/tens/{sector}")
    public List<Cita> getCitasByTensLogueado(@PathVariable String sector) {
        List<Cita> citas = citaRepository.findBySector("Sector " + sector);
    
        List<String> tiposPermitidos = Arrays.asList(
            "control cardiovascular",
            "Otras Morbilidades",
            "Morbilidad Adulto",
            "Examenes de Morbilidad"
        );
    
        return citas.stream()
            .filter(cita -> cita.getEstadoTermino() == null || !cita.getEstadoTermino())
            .filter(cita -> cita.getEstado().equalsIgnoreCase("Preparacion"))
            .filter(cita -> tiposPermitidos.contains(cita.getTipoAtencion()))
            .collect(Collectors.toList());
    }

    //Obtener cita asignadas por id del profecional
    /* Funcion no utilizada, pero que permite buscar las citas por rut del usuario*/
    @GetMapping("/profesional/{profesionalId}")
    public ResponseEntity<List<Cita>> getCitasByProfesional(@PathVariable Long profesionalId) {
        List<Cita> citasProfesional = citaRepository.findByProfesionalId(profesionalId);
        return ResponseEntity.ok(citasProfesional);
    }




}

