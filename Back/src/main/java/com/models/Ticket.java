package com.models;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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

    @Column(name = "estado")
    private String estado;

    @Column(name = "hora_confirmacion")
    private LocalTime hora_confirmacion;

    @Column(name = "hora_llamada")
    private LocalTime hora_llamada;

    @Column(name = "hora_termino")
    private LocalTime hora_termino;

    @Column(name = "Fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "totem_id")
    private Totem totem;

    @OneToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;

    @JsonManagedReference
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private registroTens registroTens;

    public Ticket() {
    }

    public Ticket(Long id, String estado, LocalTime hora_confirmacion, 
                    LocalTime hora_llamada, LocalTime hora_termino, LocalDate fecha, 
                        Totem totem, Cita cita) {
                            
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Totem getTotem() {
        return totem;
    }

    public void setTotem(Totem totem) {
        this.totem = totem;
    }

    public registroTens getRegistroTens() {
        return registroTens;
    }
}
