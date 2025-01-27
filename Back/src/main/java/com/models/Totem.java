package com.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "totem")
public class Totem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sector", nullable = false, length = 50)
    private String sector;

    @OneToMany(mappedBy = "totem")
    private Set<Ticket> tickets;

    public Totem() {
    }

    public Totem(Long id, String sector) {
        this.id = id;
        this.sector = sector;
    }

    public Long getId() {
        return id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
}