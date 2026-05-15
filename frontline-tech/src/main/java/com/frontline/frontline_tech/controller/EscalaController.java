package com.frontline.frontline_tech.controller;

import com.frontline.frontline_tech.model.Escala;
import com.frontline.frontline_tech.repository.EscalaRepository;
import com.frontline.frontline_tech.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/escalas")
@CrossOrigin(origins = "*") // Permite que o frontend converse com o backend sem erros de CORS
public class EscalaController {

    @Autowired
    private EscalaRepository escalaRepository;

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public List<Escala> listarEscalas() {
        return escalaRepository.findAll();
    }

    @PostMapping
    public Escala criarEscala(@RequestBody Escala novaEscala) {
        return escalaRepository.save(novaEscala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Escala> atualizarEscala(@PathVariable Long id, @RequestBody Escala escalaAtualizada) {
        return escalaRepository.findById(id)
                .map(escala -> {
                    escala.setTitulo(escalaAtualizada.getTitulo());
                    escala.setData(escalaAtualizada.getData());
                    escala.setEquipe(escalaAtualizada.getEquipe());
                    escala.setRepertorio(escalaAtualizada.getRepertorio());

                    // PRESERVA AS CONFIRMAÇÕES DA EQUIPE
                    escala.setConfirmacoes(escalaAtualizada.getConfirmacoes());

                    Escala salva = escalaRepository.save(escala);
                    return ResponseEntity.ok(salva);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void excluirEscala(@PathVariable Long id) {
        escalaRepository.deleteById(id);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> baixarEscalaPdf(
            @RequestParam(required = false) String mes,
            @RequestParam(required = false) Long id) {
        try {
            List<Escala> escalasParaImprimir;
            String nomeArquivo = "Escalas_BeOne.pdf";

            if (id != null) {
                Escala escala = escalaRepository.findById(id).orElse(null);
                if (escala == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

                escalasParaImprimir = List.of(escala);
                nomeArquivo = "Escala_" + escala.getData() + ".pdf";

            } else if (mes != null) {
                List<Escala> todas = escalaRepository.findAll();
                escalasParaImprimir = todas.stream()
                        .filter(e -> e.getData() != null && e.getData().startsWith(mes))
                        .collect(Collectors.toList());

                nomeArquivo = "Escalas_Mes_" + mes + ".pdf";

            } else {
                escalasParaImprimir = escalaRepository.findAll();
            }

            if (escalasParaImprimir.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            byte[] pdfBytes = pdfService.gerarPdfEscala(escalasParaImprimir);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", nomeArquivo);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}