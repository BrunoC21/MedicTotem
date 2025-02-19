package com.Dto;

import java.time.LocalDate;

public class ActualizarCitaDto {
    private Long idCita;
    private Long idMedico;
    private String sector;
    private String horaCita;
    private LocalDate fechaCita;

    // Getters y Setters

    public Long getIdCita() { return idCita; }
    
    public Long getIdMedico() { return idMedico; }
    public void setIdMedico(Long idMedico) { this.idMedico = idMedico; }
    
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    
    public String getHoraCita() { return horaCita; }
    public void setHoraCita(String horaCita) { this.horaCita = horaCita; }
    
    public LocalDate getFechaCita() { return fechaCita; }
    public void setFechaCita(LocalDate fechaCita) { this.fechaCita = fechaCita; }
}