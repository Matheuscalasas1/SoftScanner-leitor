package com.softscanner.soft.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softscanner.soft.ExportacaoService;

@RestController
public class ExportacaoController {

    private final ExportacaoService exportacaoService;

    public ExportacaoController(ExportacaoService exportacaoService) {
        this.exportacaoService = exportacaoService;
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<byte[]> exportarExcel() throws IOException {

        byte[] arquivo = exportacaoService.exportarParaExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
            MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            )
        );
        headers.set(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=controle-radios.xlsx"
        );

        return new ResponseEntity<>(arquivo, headers, HttpStatus.OK);
    }
}
