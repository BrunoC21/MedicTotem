package com.service.CitasSimples;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cita_simple")
public class CitaSimple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipocita", length = 50)
    private String tipoCita;

    @Column(name = "nroTicket")
    private int nroTicket;

    @Column(name = "nroActual")
    private int nroActual;


    public CitaSimple() {
        this.nroTicket = 1;
        this.nroActual = 0;
    }

    public CitaSimple(String tipoCita) {
        this.tipoCita = tipoCita;
        this.nroTicket = 1;
        this.nroActual = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public int getNroTicket() {
        return nroTicket;
    }

    public void countNroTicket() {
        this.nroTicket++;
    }

    public void resetNroTicket() {
        this.nroTicket = 1;
    }

    public int getNroActual() {
        return nroActual;
    }

    public void countNroActual() {
        this.nroActual++;
    }

    public void resetNroActual() {
        this.nroActual = 0;
    }
}
