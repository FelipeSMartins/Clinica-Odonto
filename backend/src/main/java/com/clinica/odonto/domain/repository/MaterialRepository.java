package com.clinica.odonto.domain.repository;

import com.clinica.odonto.domain.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    @Query("SELECT m FROM Material m WHERE m.ativo = true ORDER BY m.nome")
    List<Material> findAllAtivos();

    @Query("SELECT m FROM Material m WHERE m.nome LIKE %:nome% AND m.ativo = true ORDER BY m.nome")
    List<Material> findByNomeContainingAndAtivo(@Param("nome") String nome);

    @Query("SELECT m FROM Material m WHERE m.categoria = :categoria AND m.ativo = true ORDER BY m.nome")
    List<Material> findByCategoriaAndAtivo(@Param("categoria") String categoria);

    @Query("SELECT m FROM Material m WHERE m.codigo LIKE %:codigo% AND m.ativo = true ORDER BY m.codigo")
    List<Material> findByCodigoContainingAndAtivo(@Param("codigo") String codigo);

    @Query("SELECT m FROM Material m WHERE m.estoqueAtual <= m.estoqueMinimo AND m.ativo = true ORDER BY m.nome")
    List<Material> findMateriaisComEstoqueBaixo();

    @Query("SELECT DISTINCT m.categoria FROM Material m WHERE m.ativo = true ORDER BY m.categoria")
    List<String> findAllCategorias();

    @Query("SELECT COUNT(m) FROM Material m WHERE m.ativo = true")
    Long countMateriaisAtivos();

    @Query("SELECT COUNT(m) FROM Material m WHERE m.estoqueAtual <= m.estoqueMinimo AND m.ativo = true")
    Long countMateriaisComEstoqueBaixo();
}