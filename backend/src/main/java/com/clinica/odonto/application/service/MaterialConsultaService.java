package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.MaterialConsultaRequest;
import com.clinica.odonto.application.dto.MaterialConsultaResponse;
import com.clinica.odonto.application.dto.MovimentacaoMaterialRequest;
import com.clinica.odonto.domain.entity.*;
import com.clinica.odonto.domain.repository.ConsultaRepository;
import com.clinica.odonto.domain.repository.MaterialConsultaRepository;
import com.clinica.odonto.domain.repository.MaterialRepository;
import com.clinica.odonto.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MaterialConsultaService {

    @Autowired
    private MaterialConsultaRepository materialConsultaRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimentacaoMaterialService movimentacaoMaterialService;

    public MaterialConsultaResponse registrarMaterialConsulta(MaterialConsultaRequest request, Long usuarioId) {
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        Consulta consulta = consultaRepository.findById(request.getConsultaId())
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verificar se há estoque suficiente
        if (material.getEstoqueAtual().compareTo(request.getQuantidadeUtilizada()) < 0) {
            throw new RuntimeException("Estoque insuficiente. Estoque atual: " + material.getEstoqueAtual());
        }

        // Calcular valor total
        BigDecimal valorTotal = material.getPrecoUnitario().multiply(request.getQuantidadeUtilizada());

        // Criar registro de material da consulta
        MaterialConsulta materialConsulta = new MaterialConsulta();
        materialConsulta.setMaterial(material);
        materialConsulta.setConsulta(consulta);
        materialConsulta.setQuantidadeUtilizada(request.getQuantidadeUtilizada());
        materialConsulta.setPrecoUnitario(material.getPrecoUnitario());
        materialConsulta.setValorTotal(valorTotal);
        materialConsulta.setUsuarioLancamento(usuario);

        MaterialConsulta materialConsultaSalvo = materialConsultaRepository.save(materialConsulta);

        // Registrar movimentação de saída do estoque
        MovimentacaoMaterialRequest movimentacaoRequest = new MovimentacaoMaterialRequest();
        movimentacaoRequest.setMaterialId(material.getId());
        movimentacaoRequest.setTipoMovimentacao(TipoMovimentacao.USO_CONSULTA);
        movimentacaoRequest.setQuantidade(request.getQuantidadeUtilizada());
        movimentacaoRequest.setConsultaId(consulta.getId());
        movimentacaoRequest.setObservacoes("Uso em consulta - Paciente: " + consulta.getPaciente().getNome());

        movimentacaoMaterialService.registrarMovimentacao(movimentacaoRequest, usuarioId);

        return converterParaResponse(materialConsultaSalvo);
    }

    @Transactional(readOnly = true)
    public List<MaterialConsultaResponse> listarTodos() {
        return materialConsultaRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<MaterialConsultaResponse> listarComPaginacao(Pageable pageable) {
        return materialConsultaRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public Optional<MaterialConsultaResponse> buscarPorId(Long id) {
        return materialConsultaRepository.findById(id)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public List<MaterialConsultaResponse> buscarPorConsulta(Long consultaId) {
        return materialConsultaRepository.findByConsultaId(consultaId)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialConsultaResponse> buscarPorMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));
        return materialConsultaRepository.findByMaterialOrderByDataUtilizacaoDesc(material)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialConsultaResponse> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return materialConsultaRepository.findByDataUtilizacaoBetweenOrderByDataUtilizacaoDesc(dataInicio, dataFim)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalMateriais(Long consultaId) {
        return materialConsultaRepository.calcularValorTotalPorConsulta(consultaId)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularQuantidadeTotalUtilizada(Long materialId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return materialConsultaRepository.calcularQuantidadeTotalUtilizada(materialId, dataInicio, dataFim)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularValorTotalMesAtual() {
        return materialConsultaRepository.calcularValorTotalMesAtual()
                .orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public Long contarMateriaisUtilizadosMesAtual() {
        return materialConsultaRepository.countMateriaisUtilizadosMesAtual();
    }

    public void removerMaterialConsulta(Long id, Long usuarioId) {
        MaterialConsulta materialConsulta = materialConsultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de material da consulta não encontrado"));

        // Registrar movimentação de entrada para devolver ao estoque
        MovimentacaoMaterialRequest movimentacaoRequest = new MovimentacaoMaterialRequest();
        movimentacaoRequest.setMaterialId(materialConsulta.getMaterial().getId());
        movimentacaoRequest.setTipoMovimentacao(TipoMovimentacao.AJUSTE_POSITIVO);
        movimentacaoRequest.setQuantidade(materialConsulta.getQuantidadeUtilizada());
        movimentacaoRequest.setConsultaId(materialConsulta.getConsulta().getId());
        movimentacaoRequest.setObservacoes("Estorno de material da consulta");

        movimentacaoMaterialService.registrarMovimentacao(movimentacaoRequest, usuarioId);

        // Remover registro
        materialConsultaRepository.delete(materialConsulta);
    }

    public MaterialConsultaResponse atualizarQuantidade(Long id, BigDecimal novaQuantidade, Long usuarioId) {
        MaterialConsulta materialConsulta = materialConsultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de material da consulta não encontrado"));

        BigDecimal quantidadeAnterior = materialConsulta.getQuantidadeUtilizada();
        BigDecimal diferenca = novaQuantidade.subtract(quantidadeAnterior);

        // Se a diferença for positiva, precisa de mais material (saída do estoque)
        // Se for negativa, sobrou material (entrada no estoque)
        if (diferenca.compareTo(BigDecimal.ZERO) != 0) {
            TipoMovimentacao tipoMovimentacao = diferenca.compareTo(BigDecimal.ZERO) > 0 
                ? TipoMovimentacao.USO_CONSULTA 
                : TipoMovimentacao.AJUSTE_POSITIVO;

            MovimentacaoMaterialRequest movimentacaoRequest = new MovimentacaoMaterialRequest();
            movimentacaoRequest.setMaterialId(materialConsulta.getMaterial().getId());
            movimentacaoRequest.setTipoMovimentacao(tipoMovimentacao);
            movimentacaoRequest.setQuantidade(diferenca.abs());
            movimentacaoRequest.setConsultaId(materialConsulta.getConsulta().getId());
            movimentacaoRequest.setObservacoes("Ajuste de quantidade utilizada na consulta");

            movimentacaoMaterialService.registrarMovimentacao(movimentacaoRequest, usuarioId);
        }

        // Atualizar registro
        materialConsulta.setQuantidadeUtilizada(novaQuantidade);
        materialConsulta.setValorTotal(materialConsulta.getPrecoUnitario().multiply(novaQuantidade));

        MaterialConsulta materialConsultaAtualizado = materialConsultaRepository.save(materialConsulta);
        return converterParaResponse(materialConsultaAtualizado);
    }

    private MaterialConsultaResponse converterParaResponse(MaterialConsulta materialConsulta) {
        MaterialConsultaResponse response = new MaterialConsultaResponse();
        response.setId(materialConsulta.getId());
        response.setMaterialId(materialConsulta.getMaterial().getId());
        response.setMaterialNome(materialConsulta.getMaterial().getNome());
        response.setMaterialCodigo(materialConsulta.getMaterial().getCodigo());
        response.setMaterialCategoria(materialConsulta.getMaterial().getCategoria());
        response.setMaterialUnidadeMedida(materialConsulta.getMaterial().getUnidadeMedida());
        response.setConsultaId(materialConsulta.getConsulta().getId());
        response.setQuantidadeUtilizada(materialConsulta.getQuantidadeUtilizada());
        response.setPrecoUnitario(materialConsulta.getPrecoUnitario());
        response.setValorTotal(materialConsulta.getValorTotal());
        response.setDataUso(materialConsulta.getDataUtilizacao());
        
        if (materialConsulta.getUsuarioLancamento() != null) {
            response.setUsuarioLancamentoId(materialConsulta.getUsuarioLancamento().getId());
            response.setUsuarioLancamentoNome(materialConsulta.getUsuarioLancamento().getNome());
        }
        
        return response;
    }
}