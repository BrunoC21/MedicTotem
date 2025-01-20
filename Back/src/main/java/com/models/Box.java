package com.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "boxes")
public class Box{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio; 

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @OneToOne(mappedBy = "box")
    private User user;

    public Box() {
    }   

    public Box(String nombre, Boolean estado, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.nombre= nombre;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime setFechaInicio(LocalDateTime fechaInicio) {
        return this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public LocalDateTime setFechaFin(LocalDateTime fechaFin) {
        return this.fechaFin = fechaFin;
    }
}
