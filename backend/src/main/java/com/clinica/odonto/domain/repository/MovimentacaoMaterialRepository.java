package com.clinica.odonto.domain.repository;

import com.clinica.odonto.domain.entity.Material;
import com.clinica.odonto.domain.entity.MovimentacaoMaterial;
import com.clinica.odonto.domain.entity.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoMaterialRepository extends JpaRepository<MovimentacaoMaterial, Long> {

    @Query("SELECT m FROM MovimentacaoMaterial m WHERE m.material = :material ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoMaterial> findByMaterialOrderByDataMovimentacaoDesc(@Param("material") Material material);

    @Query("SELECT m FROM MovimentacaoMaterial m WHERE m.tipoMovimentacao = :tipo ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoMaterial> findByTipoMovimentacaoOrderByDataMovimentacaoDesc(@Param("tipo") TipoMovimentacao tipo);

    @Query("SELECT m FROM MovimentacaoMaterial m WHERE m.dataMovimentacao BETWEEN :dataInicio AND :dataFim ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoMaterial> findByDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(
            @Param("dataInicio") LocalDateTime dataInicio, 
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT m FROM MovimentacaoMaterial m WHERE m.material = :material AND m.dataMovimentacao BETWEEN :dataInicio AND :dataFim ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoMaterial> findByMaterialAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(
            @Param("material") Material material,
            @Param("dataInicio") LocalDateTime dataInicio, 
            @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT m FROM MovimentacaoMaterial m WHERE m.consulta.id = :consultaId ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoMaterial> findByConsultaIdOrderByDataMovimentacaoDesc(@Param("consultaId") Long consultaId);

    @Query("SELECT COUNT(m) FROM MovimentacaoMaterial m WHERE EXTRACT(MONTH FROM m.dataMovimentacao) = EXTRACT(MONTH FROM CURRENT_DATE()) AND EXTRACT(YEAR FROM m.dataMovimentacao) = EXTRACT(YEAR FROM CURRENT_DATE())")
    Long countMovimentacoesNoMes();
}