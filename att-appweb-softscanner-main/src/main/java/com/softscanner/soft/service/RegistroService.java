package com.softscanner.soft.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.softscanner.soft.entity.Registro;
import com.softscanner.soft.repository.RegistroRepository;

@Service
public class RegistroService {

    private final RegistroRepository registroRepository;

    public RegistroService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    // ===============================
    // VERIFICA SE EXISTE REGISTRO EM USO
    // ===============================
    public boolean serialExiste(String serial) {
        Optional<Registro> registro =
                registroRepository.findFirstBySerialAndDataRetornoIsNullOrderByIdDesc(serial);
        return registro.isPresent();
    }

    // ===============================
    // REGISTRA NOVA SAÍDA
    // ===============================
    @Transactional
    public void registrarSaida(
            String serial,
            String acessorios,
            String responsavel,
            String telefone,
            String setor,
            String observacao,
            LocalDateTime dataHora
    ) {

        Optional<Registro> registroLivre =
                registroRepository.findFirstBySerialAndDataRetornoIsNotNullOrderByIdDesc(serial);

        if (registroLivre.isPresent()) {
            Registro registro = registroLivre.get();
            registro.setAcessorio(acessorios);
            registro.setResponsavel(responsavel);
            registro.setTelefone(telefone);
            registro.setSetor(setor);
            registro.setObservacao(observacao);
            registro.setDataSaida(dataHora);
            registro.setDataRetorno(null);

            registroRepository.save(registro);
        } else {
            Registro novoRegistro = new Registro();
            novoRegistro.setSerial(serial);
            novoRegistro.setSetor(setor);
            novoRegistro.setAcessorio(acessorios);
            novoRegistro.setResponsavel(responsavel);
            novoRegistro.setTelefone(telefone);
            novoRegistro.setObservacao(observacao);
            novoRegistro.setDataSaida(dataHora);

            registroRepository.save(novoRegistro);
        }
    }

    // ===============================
    // REGISTRA RETORNO + OBSERVAÇÃO
    // ===============================
    @Transactional
    public String registrarRetorno(
            String serial,
            String observacao,
            LocalDateTime dataHora
    ) {

        Optional<Registro> opt = registroRepository
                .findFirstBySerialAndDataRetornoIsNullOrderByIdDesc(serial);

        if (opt.isEmpty()) {
            return "❌ Nenhuma saída aberta encontrada para este rádio.";
        }

        Registro registro = opt.get();

        registro.setDataRetorno(dataHora);

        if (observacao != null && !observacao.trim().isEmpty()) {
            registro.setObservacao(observacao.trim());
        }

        registroRepository.save(registro);

        return "✅ Retorno registrado com sucesso!";
    }

    // ===============================
    // BUSCA ÚLTIMO REGISTRO POR SERIAL
    // ===============================
    public Optional<Registro> buscarPorSerial(String serial) {
        return registroRepository.findFirstBySerialOrderByIdDesc(serial);
    }

    // ===============================
    // BUSCA TODOS OS REGISTROS
    // ===============================
    @Transactional(readOnly = true)
    public List<Registro> buscarTodos() {
        return registroRepository.findAllOrderByDataSaidaDesc();
    }

    // ===============================
    // EXPORTAÇÃO
    // ===============================
    @Transactional(readOnly = true)
    public List<Registro> buscarTodosParaExportacao() {
        return registroRepository.findAll();
    }

    // ===============================
    // DUPLICATAS EM USO
    // ===============================
    public boolean temDuplicatasEmUso(String serial) {
        List<Registro> registros = registroRepository.findBySerial(serial);
        long emUso = registros.stream()
                .filter(r -> r.getDataRetorno() == null)
                .count();
        return emUso > 1;
    }

    // ===============================
    // ESTATÍSTICAS
    // ===============================
    public Long getTotalRadiosUnicos() {
        return registroRepository.countTotalRadiosUnicos();
    }

    public Long getRadiosOcupados() {
        return registroRepository.countRadiosOcupados();
    }

    public Long getRadiosDisponiveis() {
        return registroRepository.countRadiosDisponiveis();
    }

    public Map<String, Long> getEstatisticasRadios() {
        Map<String, Long> estatisticas = new HashMap<>();
        estatisticas.put("totalUnicos", getTotalRadiosUnicos());
        estatisticas.put("ocupados", getRadiosOcupados());
        estatisticas.put("disponiveis", getRadiosDisponiveis());
        return estatisticas;
    }
}
