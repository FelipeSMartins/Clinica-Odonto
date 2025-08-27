package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.MovimentacaoMaterialRequest;
import com.clinica.odonto.application.dto.MovimentacaoMaterialResponse;
import com.clinica.odonto.domain.entity.*;
import com.clinica.odonto.domain.repository.ConsultaRepository;
import com.clinica.odonto.domain.repository.MaterialRepository;
import com.clinica.odonto.domain.repository.MovimentacaoMaterialRepository;
import com.clinica.odonto.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimentacaoMaterialService {

    @Autowired
    private MovimentacaoMaterialRepository movimentacaoRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MaterialService materialService;

    public MovimentacaoMaterialResponse registrarMovimentacao(MovimentacaoMaterialRequest request, Long usuarioId) {
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Validar consulta se fornecida
        Consulta consulta = null;
        if (request.getConsultaId() != null) {
            consulta = consultaRepository.findById(request.getConsultaId())
                    .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        }

        BigDecimal estoqueAnterior = material.getEstoqueAtual();
        BigDecimal novoEstoque = calcularNovoEstoque(estoqueAnterior, request.getQuantidade(), request.getTipoMovimentacao());

        // Validar se há estoque suficiente para saídas
        if (isTipoSaida(request.getTipoMovimentacao()) && novoEstoque.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Estoque insuficiente. Estoque atual: " + estoqueAnterior);
        }

        // Criar movimentação
        MovimentacaoMaterial movimentacao = new MovimentacaoMaterial();
        movimentacao.setMaterial(material);
        movimentacao.setTipoMovimentacao(request.getTipoMovimentacao());
        movimentacao.setQuantidade(request.getQuantidade());
        movimentacao.setEstoqueAnterior(estoqueAnterior);
        movimentacao.setEstoqueAtual(novoEstoque);
        movimentacao.setObservacoes(request.getObservacoes());
        movimentacao.setUsuario(usuario);
        movimentacao.setConsulta(consulta);

        MovimentacaoMaterial movimentacaoSalva = movimentacaoRepository.save(movimentacao);

        // Atualizar estoque do material
        materialService.atualizarEstoque(material.getId(), novoEstoque);

        return converterParaResponse(movimentacaoSalva);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoMaterialResponse> listarTodas() {
        return movimentacaoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MovimentacaoMaterialResponse> listarComPaginacao(Pageable pageable) {
        return movimentacaoRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public Optional<MovimentacaoMaterialResponse> buscarPorId(Long id) {
        return movimentacaoRepository.findById(id)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoMaterialResponse> buscarPorMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));
        return movimentacaoRepository.findByMaterialOrderByDataMovimentacaoDesc(material)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoMaterialResponse> buscarPorTipoMovimentacao(TipoMovimentacao tipo) {
        return movimentacaoRepository.findByTipoMovimentacao(tipo)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoMaterialResponse> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacaoRepository.findByDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(dataInicio, dataFim)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoMaterialResponse> buscarPorMaterialEPeriodo(Long materialId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));
        return movimentacaoRepository.findByMaterialAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(material, dataInicio, dataFim)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoMaterialResponse> buscarPorConsulta(Long consultaId) {
        return movimentacaoRepository.findByConsultaId(consultaId)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long contarMovimentacoesMesAtual() {
        return movimentacaoRepository.countMovimentacoesMesAtual();
    }

    private BigDecimal calcularNovoEstoque(BigDecimal estoqueAtual, BigDecimal quantidade, TipoMovimentacao tipo) {
        switch (tipo) {
            case ENTRADA:
            case AJUSTE_POSITIVO:
                return estoqueAtual.add(quantidade);
            case SAIDA:
            case USO_CONSULTA:
            case PERDA:
            case VENCIMENTO:
            case AJUSTE_NEGATIVO:
                return estoqueAtual.subtract(quantidade);
            default:
                throw new RuntimeException("Tipo de movimentação não reconhecido: " + tipo);
        }
    }

    private boolean isTipoSaida(TipoMovimentacao tipo) {
        return tipo == TipoMovimentacao.SAIDA ||
               tipo == TipoMovimentacao.USO_CONSULTA ||
               tipo == TipoMovimentacao.PERDA ||
               tipo == TipoMovimentacao.VENCIMENTO ||
               tipo == TipoMovimentacao.AJUSTE_NEGATIVO;
    }

    private MovimentacaoMaterialResponse converterParaResponse(MovimentacaoMaterial movimentacao) {
        MovimentacaoMaterialResponse response = new MovimentacaoMaterialResponse();
        response.setId(movimentacao.getId());
        response.setMaterialId(movimentacao.getMaterial().getId());
        response.setMaterialNome(movimentacao.getMaterial().getNome());
        response.setMaterialCodigo(movimentacao.getMaterial().getCodigo());
        response.setTipoMovimentacao(movimentacao.getTipoMovimentacao());
        response.setTipoMovimentacaoDescricao(movimentacao.getTipoMovimentacao().getDescricao());
        response.setQuantidade(movimentacao.getQuantidade());
        response.setEstoqueAnterior(movimentacao.getEstoqueAnterior());
        response.setEstoqueAtual(movimentacao.getEstoqueAtual());
        response.setObservacoes(movimentacao.getObservacoes());
        response.setDataMovimentacao(movimentacao.getDataMovimentacao());
        
        if (movimentacao.getUsuario() != null) {
            response.setUsuarioId(movimentacao.getUsuario().getId());
            response.setUsuarioNome(movimentacao.getUsuario().getNome());
        }
        
        if (movimentacao.getConsulta() != null) {
            response.setConsultaId(movimentacao.getConsulta().getId());
        }
        
        return response;
    }
}