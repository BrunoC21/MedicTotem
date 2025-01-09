package com.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.Cita;
import com.repository.CitaRepository;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public void cargarDatosDesdeExcel(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();

        while (rows.hasNext()) {
            Row currentRow = rows.next();
            if (currentRow.getRowNum() == 0) {
                continue; // Saltar la fila de encabezado
            }

            Cita cita = new Cita();
            cita.setEstado(currentRow.getCell(0).getStringCellValue());
            cita.setTipoAtencion(currentRow.getCell(1).getStringCellValue());
            cita.setHoraCita(new Time(currentRow.getCell(2).getDateCellValue().getTime()));
            cita.setFechaCita(new Date(currentRow.getCell(3).getDateCellValue().getTime()));
            cita.setSector(currentRow.getCell(4).getStringCellValue());

            citaRepository.save(cita);
        }

        workbook.close();
    }
}

