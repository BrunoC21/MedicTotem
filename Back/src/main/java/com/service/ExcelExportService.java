package com.service;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
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
            "ID","Especialidad", "TipoAtencion","Estado", "Fecha", "HoraConfirmacion", "HoraLlamado", "HoraTermino",
            "tiempoEsperaMedico", "duracionAtencion", "Rut", "Dv", "Nombre", "Apellido Paterno", "Apellido Materno", "Rut Profesional",
            "Nombre Profesional", "Apellido Profesional", "TENS Nombre", "TENS Rut", 
            "TENS Hora Inicio", "TENS Hora Termino", "tiempoPreparacion", "Agendador", "Sector",
        };

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        int rowNum = 1;
        for (Ticket ticket : tickets) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(ticket.getId());
            row.createCell(1).setCellValue(ticket.getCita().getProfesional().getInstrumento());
            row.createCell(2).setCellValue(ticket.getCita().getTipoAtencion());
            row.createCell(3).setCellValue(ticket.getEstado());
            row.createCell(4).setCellValue(ticket.getFecha().toString());
            row.createCell(5).setCellValue(ticket.getHora_confirmacion() != null ? 
            ticket.getHora_confirmacion().toString() : "");
            row.createCell(6).setCellValue(ticket.getHora_llamada() != null ? 
            ticket.getHora_llamada().toString() : "");
            row.createCell(7).setCellValue(ticket.getHora_termino() != null ? 
            ticket.getHora_termino().toString() : "");

            if (ticket.getHora_confirmacion() != null && ticket.getHora_llamada() != null) {
                long espera = ChronoUnit.SECONDS.between(ticket.getHora_confirmacion(), ticket.getHora_llamada());
                String tiempoFormateado = String.format("%02d:%02d",   
                    espera / 60,    // minutos
                    espera % 60              // segundos
                );
                row.createCell(8).setCellValue(tiempoFormateado);
            } else {
                row.createCell(8).setCellValue("");
            }

            if (ticket.getHora_llamada() != null && ticket.getHora_termino() != null) {
                long atencion = ChronoUnit.SECONDS.between(ticket.getHora_llamada(), ticket.getHora_termino());
                String tiempoFormateado2 = String.format("%02d:%02d",
                    atencion / 60,    // minutos
                    atencion % 60              // segundos
                );
                row.createCell(9).setCellValue(tiempoFormateado2);
            } else {
                row.createCell(9).setCellValue("");
            }

            row.createCell(10).setCellValue(ticket.getCita().getPaciente().getRut());
            row.createCell(11).setCellValue(ticket.getCita().getPaciente().getDv());
            row.createCell(12).setCellValue(ticket.getCita().getPaciente().getNombre());
            row.createCell(13).setCellValue(ticket.getCita().getPaciente().getApellido());
            row.createCell(14).setCellValue(ticket.getCita().getPaciente().getApellidoMaterno());
            row.createCell(15).setCellValue(ticket.getCita().getProfesional().getRut());
            row.createCell(16).setCellValue(ticket.getCita().getProfesional().getNombre());
            row.createCell(17).setCellValue(ticket.getCita().getProfesional().getApellido());

            if (ticket.getRegistroTens() != null) {
            row.createCell(18).setCellValue(ticket.getRegistroTens().getNombre());
            row.createCell(19).setCellValue(ticket.getRegistroTens().getRut());
            row.createCell(20).setCellValue(ticket.getRegistroTens().getHoraInicio() != null ? 
                ticket.getRegistroTens().getHoraInicio().toString() : "");
            row.createCell(21).setCellValue(ticket.getRegistroTens().getHoraTermino() != null ? 
                ticket.getRegistroTens().getHoraTermino().toString() : "");

            if (ticket.getRegistroTens().getHoraInicio() != null && ticket.getRegistroTens().getHoraTermino() != null) {
                long tens = ChronoUnit.SECONDS.between(ticket.getRegistroTens().getHoraInicio(), ticket.getRegistroTens().getHoraTermino());
                String tiempoFormateado3 = String.format("%02d:%02d", 
                    tens / 60,    // minutos
                    tens % 60              // segundos
                );
                row.createCell(22).setCellValue(tiempoFormateado3);
            } else {
                row.createCell(22).setCellValue("");
            }
            } else {
            row.createCell(18).setCellValue("");
            row.createCell(19).setCellValue("");
            row.createCell(20).setCellValue("");
            row.createCell(21).setCellValue("");
            row.createCell(22).setCellValue("");
            }

            row.createCell(23).setCellValue(ticket.getCita().getAgendador());
            row.createCell(24).setCellValue(ticket.getTotem().getSector());
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
