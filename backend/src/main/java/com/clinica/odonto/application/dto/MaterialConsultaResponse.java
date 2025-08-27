package com.clinica.odonto.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaterialConsultaResponse {

    private Long id;
    private Long materialId;
    private String materialNome;
    private String materialCodigo;
    private String materialCategoria;
    private String materialUnidadeMedida;
    private Long consultaId;
    private BigDecimal quantidadeUtilizada;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;
    private LocalDateTime dataUso;
    private Long usuarioLancamentoId;
    private String usuarioLancamentoNome;

    public MaterialConsultaResponse() {}

    public MaterialConsultaResponse(Long id, Long materialId, String materialNome, String materialCodigo,
                                  Long consultaId, BigDecimal quantidadeUtilizada, BigDecimal precoUnitario,
                                  BigDecimal valorTotal, LocalDateTime dataUso) {
        this.id = id;
        this.materialId = materialId;
        this.materialNome = materialNome;
        this.materialCodigo = materialCodigo;
        this.consultaId = consultaId;
        this.quantidadeUtilizada = quantidadeUtilizada;
        this.precoUnitario = precoUnitario;
        this.valorTotal = valorTotal;
        this.dataUso = dataUso;
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

    public String getMaterialCategoria() {
        return materialCategoria;
    }

    public void setMaterialCategoria(String materialCategoria) {
        this.materialCategoria = materialCategoria;
    }

    public String getMaterialUnidadeMedida() {
        return materialUnidadeMedida;
    }

    public void setMaterialUnidadeMedida(String materialUnidadeMedida) {
        this.materialUnidadeMedida = materialUnidadeMedida;
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

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataUso() {
        return dataUso;
    }

    public void setDataUso(LocalDateTime dataUso) {
        this.dataUso = dataUso;
    }

    public Long getUsuarioLancamentoId() {
        return usuarioLancamentoId;
    }

    public void setUsuarioLancamentoId(Long usuarioLancamentoId) {
        this.usuarioLancamentoId = usuarioLancamentoId;
    }

    public String getUsuarioLancamentoNome() {
        return usuarioLancamentoNome;
    }

    public void setUsuarioLancamentoNome(String usuarioLancamentoNome) {
        this.usuarioLancamentoNome = usuarioLancamentoNome;
    }
}