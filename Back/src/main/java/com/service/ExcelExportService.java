package com.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.models.AsistenciaMedica;
import com.models.Ticket;
import com.repository.AsistenciaRepository;
import com.repository.TicketRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ExcelExportService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    public void exportarTicketsAExcel(HttpServletResponse response) throws IOException {
        List<Ticket> tickets = ticketRepository.findAll();
        List<AsistenciaMedica> asistencias = asistenciaRepository.findAll();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datos");
        
        // Crear estilo para cabeceras
         CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Crear cabeceras
        Row headerRow = sheet.createRow(0);
        String[] columns = {
            "ID", "TipoAtencion","Estado", "Fecha", "HoraConfirmacion", "HoraLlamado", "HoraTermino",
            "Rut", "Dv", "Nombre", "Apellido Paterno", "Apellido Materno", "Rut Profesional",
            "Nombre Profesional", "Apellido Profesional", "TENS Nombre", "TENS Rut", 
            "TENS Hora Inicio", "TENS Hora Termino", "Agendador", "Sector",
        };

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
            row.createCell(1).setCellValue(ticket.getCita().getTipoAtencion());
            row.createCell(2).setCellValue(ticket.getEstado());
            row.createCell(3).setCellValue(ticket.getFecha().toString());
            row.createCell(4).setCellValue(ticket.getHora_confirmacion() != null ? 
                    ticket.getHora_confirmacion().toString() : "");
            row.createCell(5).setCellValue(ticket.getHora_llamada() != null ? 
                    ticket.getHora_llamada().toString() : "");
            row.createCell(6).setCellValue(ticket.getHora_termino() != null ? 
                    ticket.getHora_termino().toString() : "");
            row.createCell(7).setCellValue(ticket.getCita().getPaciente().getRut());   
            row.createCell(8).setCellValue(ticket.getCita().getPaciente().getDv());     
            row.createCell(9).setCellValue(ticket.getCita().getPaciente().getNombre());
            row.createCell(10).setCellValue(ticket.getCita().getPaciente().getApellido());
            row.createCell(11).setCellValue(ticket.getCita().getPaciente().getApellidoMaterno());
            row.createCell(12).setCellValue(ticket.getCita().getProfesional().getRut());    
            row.createCell(13).setCellValue(ticket.getCita().getProfesional().getNombre());
            row.createCell(14).setCellValue(ticket.getCita().getProfesional().getApellido());
            
            if (ticket.getRegistroTens() != null) {
                row.createCell(15).setCellValue(ticket.getRegistroTens().getNombre());
                row.createCell(16).setCellValue(ticket.getRegistroTens().getRut());
                row.createCell(17).setCellValue(ticket.getRegistroTens().getHoraInicio() != null ? 
                    ticket.getRegistroTens().getHoraInicio().toString() : "");
                row.createCell(18).setCellValue(ticket.getRegistroTens().getHoraTermino() != null ? 
                    ticket.getRegistroTens().getHoraTermino().toString() : "");
            } else {
                row.createCell(15).setCellValue("");
                row.createCell(16).setCellValue("");
                row.createCell(17).setCellValue("");
                row.createCell(18).setCellValue("");
            }

            row.createCell(19).setCellValue(ticket.getCita().getAgendador());   
            row.createCell(20).setCellValue(ticket.getTotem().getSector());
        }
        
        // Autoajustar columnas
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Crear segunda hoja para Asistencia
        Sheet sheetAsistencia = workbook.createSheet("Asistencia Medica");
        String[] columnsCitas = {"ID", "Fecha", "HoraInicio", "HoraTermino", "RUT", "Nombre", "Apellido"};
        
        // Ajuste de cabeceras
        Row headerRowCitas = sheetAsistencia.createRow(0);
        for (int i = 0; i < columnsCitas.length; i++) {
            Cell cell = headerRowCitas.createCell(i);
            cell.setCellValue(columnsCitas[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos para Asistencia
        int rowNumCitas = 1;
        for (AsistenciaMedica asistencia : asistencias) {
            Row row = sheetAsistencia.createRow(rowNumCitas++);
            row.createCell(0).setCellValue(asistencia.getId());
            row.createCell(1).setCellValue(asistencia.getFecha().toString());
            row.createCell(2).setCellValue(asistencia.getHoraInicio() != null ? 
                    asistencia.getHoraInicio().toString() : "");
            row.createCell(3).setCellValue(asistencia.getHoraTermino() != null ? 
                    asistencia.getHoraTermino().toString() : "");
            row.createCell(4).setCellValue(asistencia.getProfesional().getRut()); 
            row.createCell(5).setCellValue(asistencia.getProfesional().getNombre());
            row.createCell(6).setCellValue(asistencia.getProfesional().getApellido()); 
        }
        
        // Autoajustar columnas para Aistencia
        for (int i = 0; i < columnsCitas.length; i++) {
            sheetAsistencia.autoSizeColumn(i);
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
