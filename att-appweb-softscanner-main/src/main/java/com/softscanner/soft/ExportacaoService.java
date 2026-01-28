package com.softscanner.soft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.softscanner.soft.entity.Registro;
import com.softscanner.soft.service.RegistroService;

@Service
public class ExportacaoService {

    private final RegistroService registroService;

    public ExportacaoService(RegistroService registroService) {
        this.registroService = registroService;
    }

    public byte[] exportarParaExcel() throws IOException {

        List<Registro> registros = registroService.buscarTodosParaExportacao();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Controle de Rádios");

            // ===== Estilo do cabeçalho =====
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // ===== Estilo das células =====
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            // ===== Cabeçalho =====
            String[] headers = {
                "ID", "SERIAL", "SETOR", "ACESSORIO", "RESPONSÁVEL",
                "TELEFONE", "DATA SAÍDA", "DATA RETORNO", "OBSERVACAO", "STATUS"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            // ===== Dados =====
            int rowNum = 1;
            for (Registro registro : registros) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(registro.getId());
                row.createCell(1).setCellValue(registro.getSerial());
                row.createCell(2).setCellValue(registro.getSetor());
                row.createCell(3).setCellValue(registro.getAcessorio());
                row.createCell(4).setCellValue(registro.getResponsavel());
                row.createCell(5).setCellValue(registro.getTelefone());

                row.createCell(6).setCellValue(
                    registro.getDataSaida() != null
                        ? registro.getDataSaida().format(formatter)
                        : ""
                );

                row.createCell(7).setCellValue(
                    registro.getDataRetorno() != null
                        ? registro.getDataRetorno().format(formatter)
                        : ""
                );

                row.createCell(8).setCellValue(
                    registro.getObservacao() != null
                        ? registro.getObservacao()
                        : ""
                );

                String status =
                    registro.getDataRetorno() == null ? "EM USO" : "LIVRE";
                row.createCell(9).setCellValue(status);

                // aplica estilo em todas as células da linha
                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(cellStyle);
                }
            }

            // ===== Ajuste de colunas =====
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
