package com.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cita_simple")
public class CitaSimple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCitaSimple;

    @Column(name = "tipocita", nullable = false, length = 50)
    private String tipoCita;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    public CitaSimple() {
    }

    public CitaSimple(Long idCitaSimple, String tipoCita, Paciente paciente) {
        this.idCitaSimple = idCitaSimple;
        this.tipoCita = tipoCita;
        this.paciente = paciente;
    }

    public Long getIdCitaSimple() {
        return idCitaSimple;
    }

    public void setIdCitaSimple(Long idCitaSimple) {
        this.idCitaSimple = idCitaSimple;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
