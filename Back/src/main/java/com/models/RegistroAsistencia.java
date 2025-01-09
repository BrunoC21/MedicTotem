package com.models;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro_asistencia")
public class RegistroAsistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_finalizacion", nullable = false)
    private Date fechaFinalizacion;

    @Column(name = "tiempo_espera", nullable = false)
    private Time tiempoEspera;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "observacion", length = 255)
    private String observacion;

    public RegistroAsistencia() {
    }

    public RegistroAsistencia(Long id, Date fechaFinalizacion, Time tiempoEspera, Boolean estado, String observacion) {
        this.id = id;
        this.fechaFinalizacion = fechaFinalizacion;
        this.tiempoEspera = tiempoEspera;
        this.estado = estado;
        this.observacion = observacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Time getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(Time tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
