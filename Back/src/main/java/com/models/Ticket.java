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
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado", nullable = false)
    private boolean estado;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fecha_emision;

    @Column(name = "fecha_fin", nullable = true)
    private LocalDateTime fecha_fin;

    @ManyToOne
    @JoinColumn(name = "totem_id", nullable = false)
    private Totem totem;

    public Ticket() {
    }

    public Ticket(Long id, boolean estado, LocalDateTime fecha_emision, LocalDateTime fecha_fin) {
        this.id = id;
        this.estado = estado;
        this.fecha_emision = fecha_emision;
        this.fecha_fin = fecha_fin;
    }

    public Long getId() {
        return id;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(LocalDateTime fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public LocalDateTime getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDateTime fecha_fin) {
        this.fecha_fin = fecha_fin;
    }
}
