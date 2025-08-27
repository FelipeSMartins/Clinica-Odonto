package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.MaterialRequest;
import com.clinica.odonto.application.dto.MaterialResponse;
import com.clinica.odonto.domain.entity.Material;
import com.clinica.odonto.domain.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public MaterialResponse criarMaterial(MaterialRequest request) {
        // Verificar se código já existe
        if (materialRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("Código já cadastrado no sistema");
        }

        Material material = new Material();
        material.setNome(request.getNome());
        material.setCodigo(request.getCodigo());
        material.setCategoria(request.getCategoria());
        material.setUnidadeMedida(request.getUnidadeMedida());
        material.setEstoqueAtual(request.getEstoqueAtual());
        material.setEstoqueMinimo(request.getEstoqueMinimo());
        material.setPrecoUnitario(request.getPrecoUnitario());
        material.setDescricao(request.getDescricao());

        Material materialSalvo = materialRepository.save(material);
        return converterParaResponse(materialSalvo);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> listarTodos() {
        return materialRepository.findAllAtivos()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MaterialResponse> listarComPaginacao(Pageable pageable) {
        return materialRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public Optional<MaterialResponse> buscarPorId(Long id) {
        return materialRepository.findById(id)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> buscarPorNome(String nome) {
        return materialRepository.findByNomeContainingIgnoreCaseAndAtivo(nome, true)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<MaterialResponse> buscarPorCodigo(String codigo) {
        return materialRepository.findByCodigo(codigo)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> buscarPorCategoria(String categoria) {
        return materialRepository.findByCategoriaContainingIgnoreCaseAndAtivo(categoria, true)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> buscarMateriaisComEstoqueBaixo() {
        return materialRepository.findMateriaisComEstoqueBaixo()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> listarCategorias() {
        return materialRepository.findAllCategorias();
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> pesquisarMateriais(String termo) {
        return materialRepository.findByNomeOrCodigoOrCategoriaContaining(termo)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public MaterialResponse atualizarMaterial(Long id, MaterialRequest request) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        // Verificar se código já existe em outro material
        if (!material.getCodigo().equals(request.getCodigo()) && 
            materialRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("Código já cadastrado para outro material");
        }

        material.setNome(request.getNome());
        material.setCodigo(request.getCodigo());
        material.setCategoria(request.getCategoria());
        material.setUnidadeMedida(request.getUnidadeMedida());
        material.setEstoqueAtual(request.getEstoqueAtual());
        material.setEstoqueMinimo(request.getEstoqueMinimo());
        material.setPrecoUnitario(request.getPrecoUnitario());
        material.setDescricao(request.getDescricao());

        Material materialAtualizado = materialRepository.save(material);
        return converterParaResponse(materialAtualizado);
    }

    public void atualizarEstoque(Long materialId, BigDecimal novoEstoque) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));
        
        material.setEstoqueAtual(novoEstoque);
        materialRepository.save(material);
    }

    public void inativarMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));
        
        material.setAtivo(false);
        materialRepository.save(material);
    }

    public void ativarMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));
        
        material.setAtivo(true);
        materialRepository.save(material);
    }

    @Transactional(readOnly = true)
    public Long contarMateriaisAtivos() {
        return materialRepository.countByAtivo(true);
    }

    @Transactional(readOnly = true)
    public Long contarMateriaisComEstoqueBaixo() {
        return materialRepository.countMateriaisComEstoqueBaixo();
    }

    private MaterialResponse converterParaResponse(Material material) {
        MaterialResponse response = new MaterialResponse();
        response.setId(material.getId());
        response.setNome(material.getNome());
        response.setCodigo(material.getCodigo());
        response.setCategoria(material.getCategoria());
        response.setUnidadeMedida(material.getUnidadeMedida());
        response.setEstoqueAtual(material.getEstoqueAtual());
        response.setEstoqueMinimo(material.getEstoqueMinimo());
        response.setPrecoUnitario(material.getPrecoUnitario());
        response.setDescricao(material.getDescricao());
        response.setAtivo(material.getAtivo());
        response.setDataCadastro(material.getDataCadastro());
        response.setDataAtualizacao(material.getDataAtualizacao());
        
        // Verificar se estoque está baixo
        response.setEstoqueBaixo(material.getEstoqueAtual().compareTo(material.getEstoqueMinimo()) <= 0);
        
        return response;
    }
}