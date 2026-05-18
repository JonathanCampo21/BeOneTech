package com.frontline.frontline_tech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import java.time.LocalDateTime; // <-- IMPORT DA DATA/HORA

@Entity
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String funcao;   // Ex: Vocal, Bateria
    private String cargo;    // LIDER, PASTOR, MEMBRO
    private String whatsapp; // Número do Zap
    private String senha;    // Campo para armazenar a senha de acesso

    // =====================================
    // NOVO: CAMPO DO RADAR DE ACESSOS
    // =====================================
    private LocalDateTime ultimaAtividade;

    // =====================================
    // CAMPO GIGANTE PARA A FOTO BASE64
    // =====================================
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String fotoPerfil;

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    // --- Getters e Setters da Ultima Atividade (Radar) ---
    public LocalDateTime getUltimaAtividade() {
        return ultimaAtividade;
    }

    public void setUltimaAtividade(LocalDateTime ultimaAtividade) {
        this.ultimaAtividade = ultimaAtividade;
    }
}
