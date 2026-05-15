package com.frontline.frontline_tech.model;

import jakarta.persistence.*;

@Entity
public class Indisponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeMembro;
    private String data; // Vamos salvar no padrão "YYYY-MM-DD"

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeMembro() {
        return nomeMembro;
    }

    public void setNomeMembro(String nomeMembro) {
        this.nomeMembro = nomeMembro;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}