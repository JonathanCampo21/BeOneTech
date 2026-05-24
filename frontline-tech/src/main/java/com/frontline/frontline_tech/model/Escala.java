package com.frontline.frontline_tech.model;

import jakarta.persistence.*;

@Entity
public class Escala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo; // Ex: Culto de Celebração
    private String data; // Ex: 15/05/2026

    // =====================================
    // NOVO: DEPARTAMENTO DA ESCALA
    // =====================================
    private String departamento; // Ex: LOUVOR, MIDIA, SALT, etc.

    @Column(columnDefinition = "TEXT")
    private String repertorio;

    @Column(columnDefinition = "TEXT")
    private String equipe;

    // Guardará as confirmações (Verde/Vermelho) em JSON
    @Column(columnDefinition = "TEXT")
    private String confirmacoes;

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getRepertorio() { return repertorio; }
    public void setRepertorio(String repertorio) { this.repertorio = repertorio; }

    public String getEquipe() { return equipe; }
    public void setEquipe(String equipe) { this.equipe = equipe; }

    public String getConfirmacoes() { return confirmacoes; }
    public void setConfirmacoes(String confirmacoes) { this.confirmacoes = confirmacoes; }

    // --- Novos Getters e Setters do Departamento ---
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}
