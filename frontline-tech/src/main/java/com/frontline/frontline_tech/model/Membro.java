package com.frontline.frontline_tech.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String funcao;   // Ex: Vocal, Bateria, Câmera
    private String cargo;    // LIDER, PASTOR, MEMBRO
    private String whatsapp; // Número do Zap
    private String senha;    // Campo para armazenar a senha de acesso

    // ---> NOVO: MOCHILA DE LIDERANÇA (Diz de quais extensões ele é chefe)
    private String liderDe = "";

    // =====================================
    // LISTA DE DEPARTAMENTOS DO MEMBRO
    // =====================================
    // Usamos EAGER para o Java sempre carregar os departamentos junto com o membro no login
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "membro_departamentos", joinColumns = @JoinColumn(name = "membro_id"))
    @Column(name = "departamento")
    private List<String> departamentos = new ArrayList<>();

    // =====================================
    // CAMPO DO RADAR DE ACESSOS
    // =====================================
    private LocalDateTime ultimaAtividade;

    // =====================================
    // CAMPO GIGANTE PARA A FOTO BASE64
    // =====================================
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String fotoPerfil;

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public LocalDateTime getUltimaAtividade() { return ultimaAtividade; }
    public void setUltimaAtividade(LocalDateTime ultimaAtividade) { this.ultimaAtividade = ultimaAtividade; }

    public List<String> getDepartamentos() { return departamentos; }
    public void setDepartamentos(List<String> departamentos) { this.departamentos = departamentos; }

    // --- Getters e Setters Liderança ---
    public String getLiderDe() { return liderDe; }
    public void setLiderDe(String liderDe) { this.liderDe = liderDe; }
}
