package com.clinica.odonto.domain.repository;

import com.clinica.odonto.domain.entity.Consulta;
import com.clinica.odonto.domain.entity.Material;
import com.clinica.odonto.domain.entity.MaterialConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialConsultaRepository extends JpaRepository<MaterialConsulta, Long> {

    @Query("SELECT mc FROM MaterialConsulta mc WHERE mc.consulta = :consulta ORDER BY mc.dataUtilizacao DESC")
    List<MaterialConsulta> findByConsultaOrderByDataUtilizacaoDesc(@Param("consulta") Consulta consulta);

    @Query("SELECT mc FROM MaterialConsulta mc WHERE mc.material = :material ORDER BY mc.dataUtilizacao DESC")
    List<MaterialConsulta> findByMaterialOrderByDataUtilizacaoDesc(@Param("material") Material material);

    @Query("SELECT mc FROM MaterialConsulta mc WHERE mc.dataUtilizacao BETWEEN :dataInicio AND :dataFim ORDER BY mc.dataUtilizacao DESC")
    List<MaterialConsulta> findByDataUtilizacaoBetweenOrderByDataUtilizacaoDesc(
            @Param("dataInicio") LocalDateTime dataInicio, 
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT SUM(mc.valorTotal) FROM MaterialConsulta mc WHERE mc.consulta.id = :consultaId")
    Optional<BigDecimal> calcularValorTotalPorConsulta(@Param("consultaId") Long consultaId);

    @Query("SELECT SUM(mc.quantidadeUtilizada) FROM MaterialConsulta mc WHERE mc.material.id = :materialId AND mc.dataUtilizacao BETWEEN :dataInicio AND :dataFim")
    Optional<BigDecimal> calcularQuantidadeTotalUtilizada(
            @Param("materialId") Long materialId,
            @Param("dataInicio") LocalDateTime dataInicio, 
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT SUM(mc.valorTotal) FROM MaterialConsulta mc WHERE EXTRACT(MONTH FROM mc.dataUtilizacao) = EXTRACT(MONTH FROM CURRENT_DATE()) AND EXTRACT(YEAR FROM mc.dataUtilizacao) = EXTRACT(YEAR FROM CURRENT_DATE())")
    Optional<BigDecimal> calcularValorTotalMesAtual();

    @Query("SELECT COUNT(mc) FROM MaterialConsulta mc WHERE EXTRACT(MONTH FROM mc.dataUtilizacao) = EXTRACT(MONTH FROM CURRENT_DATE()) AND EXTRACT(YEAR FROM mc.dataUtilizacao) = EXTRACT(YEAR FROM CURRENT_DATE())")
    Long countMateriaisUtilizadosMesAtual();
}