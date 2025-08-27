package com.clinica.odonto.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class MaterialConsultaRequest {

    @NotNull(message = "Material é obrigatório")
    private Long materialId;

    @NotNull(message = "Consulta é obrigatória")
    private Long consultaId;

    @NotNull(message = "Quantidade utilizada é obrigatória")
    @Positive(message = "Quantidade utilizada deve ser positiva")
    private BigDecimal quantidadeUtilizada;

    public MaterialConsultaRequest() {}

    public MaterialConsultaRequest(Long materialId, Long consultaId, BigDecimal quantidadeUtilizada) {
        this.materialId = materialId;
        this.consultaId = consultaId;
        this.quantidadeUtilizada = quantidadeUtilizada;
    }

    // Getters e Setters
    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }

    public BigDecimal getQuantidadeUtilizada() {
        return quantidadeUtilizada;
    }

    public void setQuantidadeUtilizada(BigDecimal quantidadeUtilizada) {
        this.quantidadeUtilizada = quantidadeUtilizada;
    }
}