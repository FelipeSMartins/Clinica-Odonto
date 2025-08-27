package com.clinica.odonto.application.dto;

import com.clinica.odonto.domain.entity.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class MovimentacaoMaterialRequest {

    @NotNull(message = "Material é obrigatório")
    private Long materialId;

    @NotNull(message = "Tipo de movimentação é obrigatório")
    private TipoMovimentacao tipoMovimentacao;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private BigDecimal quantidade;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;

    private Long consultaId;

    public MovimentacaoMaterialRequest() {}

    public MovimentacaoMaterialRequest(Long materialId, TipoMovimentacao tipoMovimentacao, BigDecimal quantidade) {
        this.materialId = materialId;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }
}