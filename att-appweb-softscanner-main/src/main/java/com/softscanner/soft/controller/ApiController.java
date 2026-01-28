package com.softscanner.soft.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softscanner.soft.ExportacaoService;
import com.softscanner.soft.dto.LeituraResponse;
import com.softscanner.soft.entity.Registro;
import com.softscanner.soft.repository.RegistroRepository;
import com.softscanner.soft.service.RegistroService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RegistroService registroService;
    private final RegistroRepository registroRepository;
    private final ExportacaoService exportacaoService;

    public ApiController(
            RegistroService registroService,
            RegistroRepository registroRepository,
            ExportacaoService exportacaoService) {
        this.registroService = registroService;
        this.registroRepository = registroRepository;
        this.exportacaoService = exportacaoService;
    }

    // ===============================
    // LEITURA DO SCANNER (SAÍDA / RETORNO)
    // ===============================
    @PostMapping("/ler")
    public LeituraResponse ler(
            @RequestParam String serial,
            @RequestParam(required = false) String acessorios,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String setor,
            @RequestParam(required = false) String observacao
    ) {

        boolean emUso = registroService.serialExiste(serial);

        // ===============================
        // RETORNO DO RÁDIO (COM OBSERVAÇÃO)
        // ===============================
        if (emUso) {
    // Apenas informa que é retorno
    return new LeituraResponse("RETORNO", "Rádio em uso");
}

        // ===============================
        // SAÍDA DO RÁDIO
        // ===============================
        if (acessorios == null || acessorios.isBlank()
                || responsavel == null || responsavel.isBlank()
                || telefone == null || telefone.isBlank()
                || setor == null || setor.isBlank()) {

            // frontend entende isso como:
            // "mostrar formulário"
            return new LeituraResponse("SAIDA", null);
        }

        registroService.registrarSaida(
                serial,
                acessorios,
                responsavel,
                telefone,
                setor,
                null, // observação NÃO é salva na saída
                LocalDateTime.now()
        );

        return new LeituraResponse(
                "SAIDA_OK",
                acessorios + " - " + responsavel + " [" + setor + "]"
        );
    }

    @PostMapping("/retorno")
        public ResponseEntity<String> registrarRetorno(
        @RequestBody Map<String, String> body
        ) {
        String serial = body.get("serial");
        String observacao = body.get("observacao");

        String msg = registroService.registrarRetorno(
            serial,
            observacao,
            LocalDateTime.now()
        );

    return ResponseEntity.ok(msg);
    }


    // ===============================
    // EXPORTAÇÃO EXCEL
    // ===============================
    @GetMapping("/exportar-excel")
    public ResponseEntity<ByteArrayResource> exportarExcel() {
        try {
            byte[] excelBytes = exportacaoService.exportarParaExcel();
            ByteArrayResource resource = new ByteArrayResource(excelBytes);

            String filename = "controle_radios_"
                    + LocalDateTime.now().format(
                            java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(excelBytes.length)
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ===============================
    // HISTÓRICO POR SERIAL
    // ===============================
    @GetMapping("/historico/{serial}")
    public List<Map<String, Object>> buscarHistorico(@PathVariable String serial) {

        Optional<Registro> registroOpt = registroService.buscarPorSerial(serial);

        return registroOpt.stream().map(reg -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", reg.getId());
            map.put("serial", reg.getSerial());
            map.put("acessorio", reg.getAcessorio());
            map.put("responsavel", reg.getResponsavel());
            map.put("telefone", reg.getTelefone());
            map.put("setor", reg.getSetor());
            map.put("observacao", reg.getObservacao());
            map.put("dataSaida", reg.getDataSaida());
            map.put("dataRetorno", reg.getDataRetorno());
            map.put("status", reg.getDataRetorno() == null ? "EM USO" : "LIVRE");
            return map;
        }).collect(Collectors.toList());
    }

    // ===============================
    // EXCLUIR REGISTRO POR ID
    // ===============================
    @DeleteMapping("/excluir/{id}")
    public String excluirRegistro(@PathVariable Long id) {
        if (!registroRepository.existsById(id)) {
            return "⚠️ Registro não encontrado!";
        }
        registroRepository.deleteById(id);
        return "✅ Registro excluído com sucesso!";
    }

    // ===============================
    // EXCLUIR TODOS POR SERIAL
    // ===============================
    @DeleteMapping("/excluir-serial/{serial}")
    public String excluirPorSerial(@PathVariable String serial) {

        List<Registro> registros = registroRepository.findBySerial(serial);

        if (registros.isEmpty()) {
            return "⚠️ Nenhum registro encontrado para o serial " + serial;
        }

        registroRepository.deleteAll(registros);
        return "✅ " + registros.size() + " registro(s) removido(s)";
    }

    // ===============================
    // ESTATÍSTICAS
    // ===============================
    @GetMapping("/estatisticas")
    public Map<String, Long> estatisticas() {
        return registroService.getEstatisticasRadios();
    }
}
