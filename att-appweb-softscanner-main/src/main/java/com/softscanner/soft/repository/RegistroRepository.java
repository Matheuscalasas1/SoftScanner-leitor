package com.softscanner.soft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.softscanner.soft.entity.Registro;

public interface RegistroRepository extends JpaRepository<Registro, Long> {

    // ===============================
    // BUSCAS POR SERIAL
    // ===============================

    Optional<Registro> findFirstBySerialAndDataRetornoIsNullOrderByIdDesc(String serial);

    Optional<Registro> findFirstBySerialAndDataRetornoIsNotNullOrderByIdDesc(String serial);

    Optional<Registro> findFirstBySerialOrderByIdDesc(String serial);

    List<Registro> findBySerial(String serial);

    List<Registro> findBySerialOrderByIdDesc(String serial);

    // ===============================
    // LISTAGEM
    // ===============================

    @Query("SELECT r FROM Registro r ORDER BY r.dataSaida DESC")
    List<Registro> findAllOrderByDataSaidaDesc();

    // ===============================
    // ESTATÍSTICAS
    // ===============================

    @Query("SELECT COUNT(DISTINCT r.serial) FROM Registro r")
    Long countTotalRadiosUnicos();

    @Query("SELECT COUNT(r) FROM Registro r WHERE r.dataRetorno IS NULL")
    Long countRadiosOcupados();

    @Query("SELECT COUNT(r) FROM Registro r WHERE r.dataRetorno IS NOT NULL")
    Long countRadiosDisponiveis();
}
