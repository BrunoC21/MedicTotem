package com.models;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado", nullable = false)
    private boolean estado;

    @Column(name = "hora_confirmacion", nullable = false)
    private LocalTime hora_confirmacion;

    @Column(name = "hora_llamada", nullable = true)
    private LocalTime hora_llamada;

    @Column(name = "hora_termino", nullable = true)
    private LocalTime hora_termino;

    @Column(name = "Fecha", nullable = true)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "totem_id", nullable = false)
    private Totem totem;

    @OneToOne
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    public Ticket() {
    }

    public Ticket(Long id, boolean estado, LocalTime hora_confirmacion, LocalTime hora_llamada, LocalTime hora_termino, LocalDateTime fecha, Totem totem, Cita cita) {
        this.id = id;
        this.estado = estado;
        this.hora_confirmacion = hora_confirmacion;
        this.hora_llamada = hora_llamada;
        this.hora_termino = hora_termino;
        this.fecha = fecha;
        this.totem = totem;
        this.cita = cita;
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

    public LocalTime getHora_confirmacion() {
        return hora_confirmacion;
    }

    public void setHora_confirmacion(LocalTime hora_confirmacion) {
        this.hora_confirmacion = hora_confirmacion;
    }

    public LocalTime getHora_llamada() {
        return hora_llamada;
    }

    public void setHora_llamada(LocalTime hora_llamada) {
        this.hora_llamada = hora_llamada;
    }

    public LocalTime getHora_termino() {
        return hora_termino;
    }

    public void setHora_termino(LocalTime hora_termino) {
        this.hora_termino = hora_termino;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
