package com.clinica.odonto.domain.repository;

import com.clinica.odonto.domain.entity.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    Optional<Dentista> findByCro(String cro);

    boolean existsByCro(String cro);

    @Query("SELECT d FROM Dentista d WHERE d.ativo = true ORDER BY d.usuario.nome")
    List<Dentista> findAllAtivos();

    @Query("SELECT d FROM Dentista d WHERE d.usuario.nome LIKE %:nome% AND d.ativo = true ORDER BY d.usuario.nome")
    List<Dentista> findByNomeContainingAndAtivo(@Param("nome") String nome);

    @Query("SELECT d FROM Dentista d WHERE d.especialidade LIKE %:especialidade% AND d.ativo = true ORDER BY d.usuario.nome")
    List<Dentista> findByEspecialidadeContainingAndAtivo(@Param("especialidade") String especialidade);

    @Query("SELECT COUNT(d) FROM Dentista d WHERE d.ativo = true")
    Long countDentistasAtivos();

    @Query("SELECT d FROM Dentista d WHERE d.usuario.id = :usuarioId")
    Optional<Dentista> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}