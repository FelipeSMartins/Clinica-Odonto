package com.clinica.odonto.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MaterialResponse {

    private Long id;
    private String nome;
    private String codigo;
    private String categoria;
    private String unidadeMedida;
    private BigDecimal estoqueAtual;
    private BigDecimal estoqueMinimo;
    private BigDecimal precoUnitario;
    private String descricao;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    private Boolean estoqueBaixo;

    public MaterialResponse() {}

    public MaterialResponse(Long id, String nome, String codigo, String categoria, String unidadeMedida,
                           BigDecimal estoqueAtual, BigDecimal estoqueMinimo, BigDecimal precoUnitario) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.categoria = categoria;
        this.unidadeMedida = unidadeMedida;
        this.estoqueAtual = estoqueAtual;
        this.estoqueMinimo = estoqueMinimo;
        this.precoUnitario = precoUnitario;
        this.estoqueBaixo = estoqueAtual.compareTo(estoqueMinimo) <= 0;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(BigDecimal estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
        if (estoqueAtual != null && estoqueMinimo != null) {
            this.estoqueBaixo = estoqueAtual.compareTo(estoqueMinimo) <= 0;
        }
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
        if (estoqueAtual != null && estoqueMinimo != null) {
            this.estoqueBaixo = estoqueAtual.compareTo(estoqueMinimo) <= 0;
        }
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Boolean getEstoqueBaixo() {
        return estoqueBaixo;
    }

    public void setEstoqueBaixo(Boolean estoqueBaixo) {
        this.estoqueBaixo = estoqueBaixo;
    }
}