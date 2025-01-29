package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.Cita;
import com.models.Paciente;
import com.models.User;
import com.repository.CitaRepository;
import com.repository.PacienteRepository;
import com.repository.UserRepository;

@Service
public class ExcelService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public void cargarDatosDesdeExcel(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        //Dar formato a la fecha del documento a uno aceptable por la base de datos
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (currentRow.getRowNum() < 11) {
                continue; // Saltar la fila de encabezado
            }

            //Rellena Datos para entidad Paciente
            Paciente paciente = new Paciente();
            paciente.setRut(currentRow.getCell(34).getStringCellValue());
            paciente.setDv(currentRow.getCell(35).getStringCellValue());
            paciente.setNombre(currentRow.getCell(31).getStringCellValue());
            paciente.setApellido(currentRow.getCell(32).getStringCellValue());
            paciente.setApellidoMaterno(currentRow.getCell(33).getStringCellValue());
            paciente.setSexo(currentRow.getCell(38).getStringCellValue());
            paciente.setDireccion(currentRow.getCell(46).getStringCellValue());

            pacienteRepository.save(paciente);
            
            //Agregar Datos para Entidad Cita
            Cita cita = new Cita();
            cita.setEstado(currentRow.getCell(16).getStringCellValue());
            cita.setTipoAtencion(currentRow.getCell(20).getStringCellValue());
            cita.setHoraCita(new Time(currentRow.getCell(10).getDateCellValue().getTime()));
            
            String fechaCitaStr = currentRow.getCell(9).getStringCellValue();
            LocalDate fechaCita = LocalDate.parse(fechaCitaStr, dateFormatter);
            cita.setFechaCita(fechaCita);

            cita.setSector(currentRow.getCell(44).getStringCellValue());
            cita.setPaciente(paciente);

            //Buscar profesional por RUT para agragar a cita
            String rutProfesional = currentRow.getCell(27).getStringCellValue();
            User profesional = userRepository.findByRut(rutProfesional);
            if (profesional != null) {
                cita.setProfesional(profesional);
            } else {
                // Manejar el caso en que el profesional no se encuentre
                System.out.println("Profesional con RUT " + rutProfesional + " no encontrado.");
            } 

            citaRepository.save(cita); 
        }

        workbook.close();
    }

}

