package com.models;

import java.sql.Time;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado")
    private String estado;

    @Column(name = "tipo_atencion")
    private String tipoAtencion;

    @Column(name = "hora_cita")
    private Time horaCita;

    @Column(name = "fecha_cita")
    private Date fechaCita;

    @Column(name = "sector")
    private String sector;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private User profesional;

    public Cita() {
    }

    public Cita(String estado, String tipoAtencion, Time horaCita, Date fechaCita, String sector) {
        this.estado = estado;
        this.tipoAtencion = tipoAtencion;
        this.horaCita = horaCita;
        this.fechaCita = fechaCita;
        this.sector = sector;
    }

    public Long getId() {
        return id;
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

    public Time getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(Time horaCita) {
        this.horaCita = horaCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getSector(){
        return sector;
    }

    public void setSector(String sector){
        this.sector = sector;
    }

    
}
