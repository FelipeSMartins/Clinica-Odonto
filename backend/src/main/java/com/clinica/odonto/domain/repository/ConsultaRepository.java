package com.clinica.odonto.domain.repository;

import com.clinica.odonto.domain.entity.Consulta;
import com.clinica.odonto.domain.entity.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT c FROM Consulta c WHERE CAST(c.dataHora AS date) = :data ORDER BY c.dataHora")
    List<Consulta> findByData(@Param("data") LocalDate data);

    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId ORDER BY c.dataHora DESC")
    List<Consulta> findByPacienteId(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM Consulta c WHERE c.dentista.id = :dentistaId ORDER BY c.dataHora DESC")
    List<Consulta> findByDentistaId(@Param("dentistaId") Long dentistaId);

    @Query("SELECT c FROM Consulta c WHERE c.status = :status ORDER BY c.dataHora")
    List<Consulta> findByStatus(@Param("status") StatusConsulta status);

    @Query("SELECT c FROM Consulta c WHERE CAST(c.dataHora AS date) = CURRENT_DATE() ORDER BY c.dataHora")
    List<Consulta> findConsultasHoje();

    @Query("SELECT c FROM Consulta c WHERE CAST(c.dataHora AS date) = CURRENT_DATE() AND c.status = :status ORDER BY c.dataHora")
    List<Consulta> findConsultasHojePorStatus(@Param("status") StatusConsulta status);

    @Query("SELECT COUNT(c) FROM Consulta c WHERE CAST(c.dataHora AS date) = CURRENT_DATE()")
    Long countConsultasHoje();

    @Query("SELECT COUNT(c) FROM Consulta c WHERE CAST(c.dataHora AS date) = CURRENT_DATE() AND c.status = :status")
    Long countConsultasHojePorStatus(@Param("status") StatusConsulta status);

    @Query("SELECT COUNT(c) FROM Consulta c WHERE EXTRACT(MONTH FROM c.dataHora) = EXTRACT(MONTH FROM CURRENT_DATE()) AND EXTRACT(YEAR FROM c.dataHora) = EXTRACT(YEAR FROM CURRENT_DATE())")
    Long countConsultasNoMes();

    @Query("SELECT SUM(c.valor) FROM Consulta c WHERE EXTRACT(MONTH FROM c.dataHora) = EXTRACT(MONTH FROM CURRENT_DATE()) AND EXTRACT(YEAR FROM c.dataHora) = EXTRACT(YEAR FROM CURRENT_DATE()) AND c.status = 'CONCLUIDA'")
    BigDecimal somaFaturamentoMensal();

    @Query("SELECT c FROM Consulta c WHERE c.dataHora BETWEEN :inicio AND :fim ORDER BY c.dataHora")
    List<Consulta> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT c FROM Consulta c WHERE c.dentista.id = :dentistaId AND CAST(c.dataHora AS date) BETWEEN :dataInicio AND :dataFim ORDER BY c.dataHora")
    List<Consulta> findByDentistaAndPeriodo(@Param("dentistaId") Long dentistaId, @Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}