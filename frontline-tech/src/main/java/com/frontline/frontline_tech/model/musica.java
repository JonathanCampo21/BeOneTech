package com.frontline.frontline_tech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // Essa anotação avisa o Spring: "Ei, transforme isso numa tabela no MySQL!"
public class musica {

    @Id // Diz que esse é o ID da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Faz o ID ser Auto Increment
    private Long id;

    private String titulo;
    private String ministro;
    private String tomOriginal;
    private Integer bpm;

    // Construtor vazio (obrigatório para o Spring)
    public musica() {
    }

    // --- DAQUI PARA BAIXO, SÃO OS GETTERS E SETTERS ---
    // Você pode colar isso, ou gerar automático no IntelliJ apertando (Alt + Insert) > Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMinistro() {
        return ministro;
    }

    public void setMinistro(String ministro) {
        this.ministro = ministro;
    }

    public String getTomOriginal() {
        return tomOriginal;
    }

    public void setTomOriginal(String tomOriginal) {
        this.tomOriginal = tomOriginal;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
    }

    private Integer quantidadeSugestoes;

    public Integer getQuantidadeSugestoes() {
        return quantidadeSugestoes;
    }

    public void setQuantidadeSugestoes(Integer quantidadeSugestoes) {
        this.quantidadeSugestoes = quantidadeSugestoes;
    }
}