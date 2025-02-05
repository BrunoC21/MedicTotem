package com.controllers;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.repository.CitaRepository;
import com.repository.PacienteRepository;
import com.repository.TicketRepository;
import com.service.ExcelExportService;
import com.service.ExcelService;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/excel")
public class ExcelController {
    
    @Autowired
    private ExcelService excelService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Exportar datos a excel
    @GetMapping("/exportar")
    public void exportarAExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=datos.xlsx");
        excelExportService.exportarTicketsAExcel(response);
    }

    // Cargar datos del excel
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

    @DeleteMapping("/eliminar-datos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarTodosLosDatos() {
        try {
            // Eliminar en orden por dependencias
            ticketRepository.deleteAll();
            citaRepository.deleteAll();
            pacienteRepository.deleteAll();
            
            return ResponseEntity.ok()
                .body(Map.of("mensaje", "Todos los datos han sido eliminados exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al eliminar los datos: " + e.getMessage()));
        }
        
    }
}
