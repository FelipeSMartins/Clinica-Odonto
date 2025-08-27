package com.clinica.odonto.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class MaterialRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 2, max = 20, message = "Código deve ter entre 2 e 20 caracteres")
    private String codigo;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    private String categoria;

    @NotBlank(message = "Unidade de medida é obrigatória")
    @Size(max = 10, message = "Unidade de medida deve ter no máximo 10 caracteres")
    private String unidadeMedida;

    @NotNull(message = "Estoque atual é obrigatório")
    @PositiveOrZero(message = "Estoque atual deve ser zero ou positivo")
    private BigDecimal estoqueAtual;

    @NotNull(message = "Estoque mínimo é obrigatório")
    @PositiveOrZero(message = "Estoque mínimo deve ser zero ou positivo")
    private BigDecimal estoqueMinimo;

    @NotNull(message = "Preço unitário é obrigatório")
    @PositiveOrZero(message = "Preço unitário deve ser zero ou positivo")
    private BigDecimal precoUnitario;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    public MaterialRequest() {}

    public MaterialRequest(String nome, String codigo, String categoria, String unidadeMedida,
                          BigDecimal estoqueAtual, BigDecimal estoqueMinimo, BigDecimal precoUnitario) {
        this.nome = nome;
        this.codigo = codigo;
        this.categoria = categoria;
        this.unidadeMedida = unidadeMedida;
        this.estoqueAtual = estoqueAtual;
        this.estoqueMinimo = estoqueMinimo;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
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
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
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
}