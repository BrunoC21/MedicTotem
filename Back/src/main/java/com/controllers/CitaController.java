package com.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/all")
    public List<Cita> listaTodosBox() {
        return citaRepository.findAll();
    }

    @GetMapping("/paciente/{rut}/{sector}")
        public List<Cita> obtenerCitasPorRutPaciente(@PathVariable String rut, @PathVariable String sector) {
        return citaRepository.findByPacienteRutAndSector(rut, "Sector " + sector);
    }

    //Metodo para cargar datos de pacientes y de citas desde un archivo excel
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

    @PutMapping("/actualizarEstado/{id}")
    public String actualizarEstadoCita(@PathVariable Long id) {
        Optional<Cita> citaOptional = citaRepository.findById(id);
        if (citaOptional.isPresent()) {
            Cita cita = citaOptional.get();
            cita.setEstado("Completado");
            citaRepository.save(cita);
            return "Estado de la cita actualizado exitosamente";
        } else {
            return "Cita no encontrada";
        }
    }
}

