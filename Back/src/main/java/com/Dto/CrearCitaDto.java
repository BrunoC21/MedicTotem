package com.Dto;

import java.sql.Time;
import java.time.LocalDate;

public class CrearCitaDto {
    private String estado;
    private String tipoAtencion;
    private String instrumento;
    private Time horaCita;
    private LocalDate fechaCita;
    private String sector;
    private String agendador;

    // Datos del profecional
    private Long idProfecional;
    
    // Datos del paciente
    private String rutPaciente;
    private String dvPaciente;
    private String nombrePaciente;
    private String apellidoPaternoPaciente;
    private String apellidoMaternoPaciente;
    private String sexo;

    public CrearCitaDto() {
    }

    public Long getIdProfecional() {
        return idProfecional;
    }

    public void setIdProfecional(Long idProfecional) {
        this.idProfecional = idProfecional;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoAtencion() {
        return tipoAtencion;
    }

    public void setTipoAtencion(String tipoAtencion) {
        this.tipoAtencion = tipoAtencion;
    }

    public String getInstrumento() {
        return instrumento;
    }

    public void setInstrumento(String instrumento) {
        this.instrumento = instrumento;
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

    public String getAgendador() {
        return agendador;
    }

    public void setAgendador(String agendador) {
        this.agendador = agendador;
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
