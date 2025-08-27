package com.clinica.odonto.domain.repository;

import com.clinica.odonto.domain.entity.PlanoSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoSaudeRepository extends JpaRepository<PlanoSaude, Long> {

    Optional<PlanoSaude> findByCodigoAns(String codigoAns);

    boolean existsByCodigoAns(String codigoAns);

    @Query("SELECT p FROM PlanoSaude p WHERE p.ativo = true ORDER BY p.nome")
    List<PlanoSaude> findAllAtivos();

    @Query("SELECT p FROM PlanoSaude p WHERE p.nome LIKE %:nome% AND p.ativo = true ORDER BY p.nome")
    List<PlanoSaude> findByNomeContainingAndAtivo(@Param("nome") String nome);

    @Query("SELECT COUNT(p) FROM PlanoSaude p WHERE p.ativo = true")
    Long countPlanosAtivos();

    @Query("SELECT p FROM PlanoSaude p JOIN p.dentistas d WHERE d.id = :dentistaId AND p.ativo = true")
    List<PlanoSaude> findByDentistaId(@Param("dentistaId") Long dentistaId);

    @Query("SELECT p FROM PlanoSaude p WHERE p.id IN (SELECT pac.planoSaude.id FROM Paciente pac WHERE pac.planoSaude IS NOT NULL) AND p.ativo = true")
    List<PlanoSaude> findPlanosComPacientes();
}