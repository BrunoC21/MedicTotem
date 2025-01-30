package com.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.Ticket;
import com.repository.TicketRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExcelExportService {

    @Autowired
    private TicketRepository ticketRepository;

    public void exportarTicketsAExcel(HttpServletResponse response) throws IOException {
        List<Ticket> tickets = ticketRepository.findAll();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datos");
        
        // Crear estilo para cabeceras
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Crear cabeceras
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Estado", "Fecha", "HoraConfirmacion", "HoraLlamado", "HoraTermino", "Sector"};
        
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        int rowNum = 1;
        for(Ticket ticket: tickets) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(ticket.getId());
            row.createCell(1).setCellValue(ticket.getEstado());
            row.createCell(2).setCellValue(ticket.getFecha().toString());
            row.createCell(3).setCellValue(ticket.getHora_confirmacion() != null ? 
                    ticket.getHora_confirmacion().toString() : "");
            row.createCell(4).setCellValue(ticket.getHora_llamada() != null ? 
                    ticket.getHora_llamada().toString() : "");
            row.createCell(5).setCellValue(ticket.getHora_termino() != null ? 
                    ticket.getHora_termino().toString() : "");
            row.createCell(6).setCellValue(ticket.getTotem().getSector());
        }
        
        // Autoajustar columnas
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Configurar respuesta HTTP
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=citas.xlsx");
        
        // Escribir al output stream
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
