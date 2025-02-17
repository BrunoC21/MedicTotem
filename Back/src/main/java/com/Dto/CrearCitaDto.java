package com.Dto;

import java.sql.Time;
import java.time.LocalDate;

public class CrearCitaDto {
    private String tipoAtencion;
    private Time horaCita;
    private LocalDate fechaCita;
    private String sector;
    private Long idMedico; // Cambiado de idProfecional a idMedico
    private String rutPaciente;
    private String dvPaciente;
    private String nombrePaciente;
    private String apellidoPaternoPaciente;
    private String apellidoMaternoPaciente;
    private String sexo;

    public CrearCitaDto() {
    }

    public Long getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Long idMedico) {
        this.idMedico = idMedico;
    }

    public String getTipoAtencion() {
        return tipoAtencion;
    }

    public void setTipoAtencion(String tipoAtencion) {
        this.tipoAtencion = tipoAtencion;
    }
    
    public Time getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getRutPaciente() {
        return rutPaciente;
    }

    public void setRutPaciente(String rutPaciente) {
        this.rutPaciente = rutPaciente;
    }

    public String getDvPaciente() {
        return dvPaciente;
    }

    public void setDvPaciente(String dvPaciente) {
        this.dvPaciente = dvPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getApellidoPaternoPaciente() {
        return apellidoPaternoPaciente;
    }   

    public void setApellidoPaternoPaciente(String apellidoPaternoPaciente) {
        this.apellidoPaternoPaciente = apellidoPaternoPaciente;
    }   

    public String getApellidoMaternoPaciente() {
        return apellidoMaternoPaciente;
    }  

    public void setApellidoMaternoPaciente(String apellidoMaternoPaciente) {
        this.apellidoMaternoPaciente = apellidoMaternoPaciente;
    }

    public String getSexo(){
        return sexo;
    }

    public void setSexo(String sexo){
        this.sexo = sexo;
    }





}
