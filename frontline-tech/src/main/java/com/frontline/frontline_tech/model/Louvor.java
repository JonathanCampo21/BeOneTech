package com.frontline.frontline_tech.model;

import jakarta.persistence.*;

@Entity
public class Louvor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String artista;
    private String tom;

    private Integer bpm;
    private String compasso; // Campo do compasso (Ex: 4/4, 6/8)

    // Mudamos para LONGTEXT para suportar a imagem em formato Base64
    @Column(columnDefinition = "LONGTEXT")
    private String imagemUrl;

    @Column(length = 1000)
    private String linkCifra;

    @Column(length = 1000)
    private String linkLetra;

    @Column(length = 1000)
    private String linkVs;

    @Column(length = 1000)
    private String linkYoutube; // Link do vídeo

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public String getTom() { return tom; }
    public void setTom(String tom) { this.tom = tom; }

    public Integer getBpm() { return bpm; }
    public void setBpm(Integer bpm) { this.bpm = bpm; }

    public String getCompasso() { return compasso; }
    public void setCompasso(String compasso) { this.compasso = compasso; }

    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }

    public String getLinkCifra() { return linkCifra; }
    public void setLinkCifra(String linkCifra) { this.linkCifra = linkCifra; }

    public String getLinkLetra() { return linkLetra; }
    public void setLinkLetra(String linkLetra) { this.linkLetra = linkLetra; }

    public String getLinkVs() { return linkVs; }
    public void setLinkVs(String linkVs) { this.linkVs = linkVs; }

    public String getLinkYoutube() { return linkYoutube; }
    public void setLinkYoutube(String linkYoutube) { this.linkYoutube = linkYoutube; }
}