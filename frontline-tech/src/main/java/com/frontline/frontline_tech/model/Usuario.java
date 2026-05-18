package com.frontline.frontline_tech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime; // <--- Import da data e hora

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true) // Garante que não existam dois logins iguais
    private String login;

    private String senha;

    private String cargo; // Vai guardar: LIDER, MEMBRO, PASTOR ou DEV

    // ---> NOVO CAMPO: O radar que vai guardar o último clique do usuário
    private LocalDateTime ultimaAtividade;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    // --- Novos Getters e Setters do Radar ---
    public LocalDateTime getUltimaAtividade() {
        return ultimaAtividade;
    }

    public void setUltimaAtividade(LocalDateTime ultimaAtividade) {
        this.ultimaAtividade = ultimaAtividade;
    }
}
