package com.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
