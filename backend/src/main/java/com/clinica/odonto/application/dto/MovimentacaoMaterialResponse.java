package com.clinica.odonto.application.dto;

import com.clinica.odonto.domain.entity.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimentacaoMaterialResponse {

    private Long id;
    private Long materialId;
    private String materialNome;
    private String materialCodigo;
    private TipoMovimentacao tipoMovimentacao;
    private String tipoMovimentacaoDescricao;
    private BigDecimal quantidade;
    private BigDecimal estoqueAnterior;
    private BigDecimal estoqueAtual;
    private String observacoes;
    private LocalDateTime dataMovimentacao;
    private Long usuarioId;
    private String usuarioNome;
    private Long consultaId;

    public MovimentacaoMaterialResponse() {}

    public MovimentacaoMaterialResponse(Long id, Long materialId, String materialNome, String materialCodigo,
                                       TipoMovimentacao tipoMovimentacao, BigDecimal quantidade,
                                       BigDecimal estoqueAnterior, BigDecimal estoqueAtual,
                                       LocalDateTime dataMovimentacao) {
        this.id = id;
        this.materialId = materialId;
        this.materialNome = materialNome;
        this.materialCodigo = materialCodigo;
        this.tipoMovimentacao = tipoMovimentacao;
        this.tipoMovimentacaoDescricao = tipoMovimentacao.getDescricao();
        this.quantidade = quantidade;
        this.estoqueAnterior = estoqueAnterior;
        this.estoqueAtual = estoqueAtual;
        this.dataMovimentacao = dataMovimentacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getMaterialNome() {
        return materialNome;
    }

    public void setMaterialNome(String materialNome) {
        this.materialNome = materialNome;
    }

    public String getMaterialCodigo() {
        return materialCodigo;
    }

    public void setMaterialCodigo(String materialCodigo) {
        this.materialCodigo = materialCodigo;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
        if (tipoMovimentacao != null) {
            this.tipoMovimentacaoDescricao = tipoMovimentacao.getDescricao();
        }
    }

    public String getTipoMovimentacaoDescricao() {
        return tipoMovimentacaoDescricao;
    }

    public void setTipoMovimentacaoDescricao(String tipoMovimentacaoDescricao) {
        this.tipoMovimentacaoDescricao = tipoMovimentacaoDescricao;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getEstoqueAnterior() {
        return estoqueAnterior;
    }

    public void setEstoqueAnterior(BigDecimal estoqueAnterior) {
        this.estoqueAnterior = estoqueAnterior;
    }

    public BigDecimal getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(BigDecimal estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }
}