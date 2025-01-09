package com.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "asistencia_medica")
public class AsistenciaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado")
    private Boolean confirmacion;

    @Column(name = "llegada")
    private LocalDateTime llegada;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private User profesional;

    public AsistenciaMedica() {
    }   

    public AsistenciaMedica(Boolean confirmacion, LocalDateTime llegada, User profesional) {
        this.confirmacion = confirmacion;
        this.llegada = llegada;
        this.profesional = profesional;
    }   

    public Long getId() {
        return id;
    }

    public Boolean getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(Boolean confirmacion) {
        this.confirmacion = confirmacion;
    }

    public LocalDateTime getLlegada() {
        return llegada;
    }

    public void setLlegada(LocalDateTime llegada) {
        this.llegada = llegada;
    }

    public User getProfesional() {
        return profesional;
    }

    public void setProfesional(User profesional) {
        this.profesional = profesional;
    }

}
