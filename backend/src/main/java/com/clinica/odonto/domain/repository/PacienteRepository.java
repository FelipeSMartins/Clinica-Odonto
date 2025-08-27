package com.clinica.odonto.domain.repository;

import com.clinica.odonto.domain.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    @Query("SELECT p FROM Paciente p WHERE p.ativo = true ORDER BY p.nome")
    List<Paciente> findAllAtivos();

    @Query("SELECT p FROM Paciente p WHERE p.nome LIKE %:nome% AND p.ativo = true ORDER BY p.nome")
    List<Paciente> findByNomeContainingAndAtivo(@Param("nome") String nome);

    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.ativo = true")
    Long countPacientesAtivos();

    @Query("SELECT COUNT(p) FROM Paciente p WHERE EXTRACT(MONTH FROM p.dataCadastro) = EXTRACT(MONTH FROM CURRENT_DATE()) AND EXTRACT(YEAR FROM p.dataCadastro) = EXTRACT(YEAR FROM CURRENT_DATE())")
    Long countPacientesCadastradosNoMes();
}