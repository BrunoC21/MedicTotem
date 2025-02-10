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


    public CitaSimple() {
        this.nroTicket = 1;
    }

    public CitaSimple(Long id, String tipoCita) {
        this.id = id;
        this.tipoCita = tipoCita;
        this.nroTicket = 1;
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

}
