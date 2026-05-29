package com.frontline.frontline_tech.service;

import com.frontline.frontline_tech.model.Escala;
import com.frontline.frontline_tech.model.Louvor;
import com.frontline.frontline_tech.model.Membro;
import com.frontline.frontline_tech.repository.EscalaRepository;
import com.frontline.frontline_tech.repository.LouvorRepository;
import com.frontline.frontline_tech.repository.MembroRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
public class PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LouvorRepository louvorRepository;

    @Autowired
    private EscalaRepository escalaRepository;

    @Autowired
    private MembroRepository membroRepository;

    // 🔨 A MARRETA: Vai rodar automaticamente e corrigir "MIDIA" para "COMUNICAÇÃO"
    @PostConstruct
    public void atualizarBancoMidiaParaComunicacao() {
        List<Escala> escalasAntigas = escalaRepository.findByDepartamento("MIDIA");
        for (Escala e : escalasAntigas) {
            e.setDepartamento("COMUNICAÇÃO");
            escalaRepository.save(e);
        }
        
        List<Membro> membrosAntigos = membroRepository.findByDepartamentosContainingOrderByNomeAsc("MIDIA");
        for (Membro m : membrosAntigos) {
            String depts = m.getDepartamentos();
            if (depts != null && depts.contains("MIDIA")) {
                m.setDepartamentos(depts.replace("MIDIA", "COMUNICAÇÃO"));
                membroRepository.save(m);
            }
        }
    }

    public byte[] gerarPdfEscala(List<Escala> escalas) throws Exception {

        List<Louvor> bancoDeLouvores = louvorRepository.findAll();
        List<Map<String, Object>> escalasFormatadas = new ArrayList<>();
        boolean isIndividual = escalas.size() == 1;

        for (Escala e : escalas) {
            if (!isIndividual && e.getTitulo() != null && e.getTitulo().contains("[LIDERANÇA]")) {
                continue;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("titulo", e.getTitulo() != null ? e.getTitulo() : "Evento sem título");
            
            // Agora o Java manda a variável pro Thymeleaf pintar a cor
            map.put("departamento", e.getDepartamento() != null ? e.getDepartamento() : "LOUVOR");

            String dataStr = e.getData();
            if (dataStr != null && dataStr.contains("-")) {
                String[] p = dataStr.split("-");
                if (p.length == 3) dataStr = p[2] + "/" + p[1] + "/" + p[0];
            }
            map.put("data", dataStr != null ? dataStr : "Data não definida");

            Map<String, String> vocalMap = new LinkedHashMap<>();
            Map<String, String> bandaMap = new LinkedHashMap<>();

            if (e.getEquipe() != null) {
                String[] membrosStr = e.getEquipe().split("\n");
                for (String linha : membrosStr) {
                    if (linha.trim().isEmpty()) continue;

                    String nome = linha;
                    String funcao = "Membro";

                    if (linha.contains("(") && linha.contains(")")) {
                        int openIdx = linha.lastIndexOf("(");
                        int closeIdx = linha.lastIndexOf(")");
                        nome = linha.substring(0, openIdx).trim();
                        funcao = linha.substring(openIdx + 1, closeIdx).trim();
                    }

                    String funcLower = funcao.toLowerCase();
                    boolean isVocal = funcLower.contains("vocal") || funcLower.contains("ministr") ||
                            funcLower.contains("backing") || funcLower.contains("voz");

                    Map<String, String> targetMap = isVocal ? vocalMap : bandaMap;

                    if (targetMap.containsKey(nome)) {
                        targetMap.put(nome, targetMap.get(nome) + ", " + funcao);
                    } else {
                        targetMap.put(nome, funcao);
                    }
                }
            }

            List<Map<String, String>> vocal = new ArrayList<>();
            for (Map.Entry<String, String> entry : vocalMap.entrySet()) {
                Map<String, String> m = new HashMap<>();
                m.put("nome", entry.getKey());
                m.put("funcao", entry.getValue());
                vocal.add(m);
            }

            List<Map<String, String>> banda = new ArrayList<>();
            for (Map.Entry<String, String> entry : bandaMap.entrySet()) {
                Map<String, String> m = new HashMap<>();
                m.put("nome", entry.getKey());
                m.put("funcao", entry.getValue());
                banda.add(m);
            }
            
            // Essa lista é para a Mídia, Recepção etc, que não divide em Banda e Vocal
            List<Map<String, String>> equipeGeral = new ArrayList<>();
            equipeGeral.addAll(vocal);
            equipeGeral.addAll(banda);
            map.put("equipeGeral", equipeGeral);

            map.put("vocal", vocal);
            map.put("banda", banda);

            List<Map<String, String>> repertorioList = new ArrayList<>();
            if (e.getRepertorio() != null && !e.getRepertorio().trim().isEmpty()) {
                String[] musicasStr = e.getRepertorio().split("\n");
                for (String linha : musicasStr) {
                    if (linha.trim().isEmpty()) continue;

                    String tituloMusica = linha;
                    String tom = "";

                    if (linha.contains("(") && linha.contains(")")) {
                        int openIdx = linha.lastIndexOf("(");
                        int closeIdx = linha.lastIndexOf(")");
                        tituloMusica = linha.substring(0, openIdx).trim();
                        tom = linha.substring(openIdx + 1, closeIdx).trim();
                    }

                    Map<String, String> m = new HashMap<>();
                    m.put("titulo", tituloMusica);
                    m.put("tom", tom);

                    String finalTitulo = tituloMusica;
                    Louvor louvorOficial = bancoDeLouvores.stream()
                            .filter(l -> l.getTitulo().equalsIgnoreCase(finalTitulo))
                            .findFirst()
                            .orElse(null);

                    if (louvorOficial != null) {
                        m.put("bpm", louvorOficial.getBpm() != null ? String.valueOf(louvorOficial.getBpm()) : "--");
                        m.put("compasso", louvorOficial.getCompasso() != null ? louvorOficial.getCompasso() : "--");
                        m.put("linkCifra", louvorOficial.getLinkCifra() != null ? louvorOficial.getLinkCifra() : "");
                        m.put("linkYoutube", louvorOficial.getLinkYoutube() != null ? louvorOficial.getLinkYoutube() : "");
                        m.put("linkVs", louvorOficial.getLinkVs() != null ? louvorOficial.getLinkVs() : "");
                        m.put("linkLetra", louvorOficial.getLinkLetra() != null ? louvorOficial.getLinkLetra() : "");
                    } else {
                        m.put("bpm", "--"); m.put("compasso", "--");
                        m.put("linkCifra", ""); m.put("linkYoutube", ""); m.put("linkVs", "");
                        m.put("linkLetra", "");
                    }

                    repertorioList.add(m);
                }
            }
            map.put("repertorio", repertorioList);

            escalasFormatadas.add(map);
        }

        Context context = new Context();
        context.setVariable("escalas", escalasFormatadas);
        context.setVariable("isIndividual", isIndividual);

        if (!escalasFormatadas.isEmpty()) {
            context.setVariable("escala", escalasFormatadas.get(0));
        } else {
            context.setVariable("escala", new HashMap<>());
        }

        // 🔀 ROTEAMENTO DE TEMPLATES 
        String templateName = "template-pdf-padrao"; 
        
        if (!escalasFormatadas.isEmpty()) {
            String dept = (String) escalasFormatadas.get(0).get("departamento");
            if ("LOUVOR".equalsIgnoreCase(dept)) {
                templateName = "template-pdf-louvor"; 
            }
        }

        String html = templateEngine.process(templateName, context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }
}
