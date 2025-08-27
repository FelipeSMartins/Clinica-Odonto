package com.clinica.odonto.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PlanoSaudeRequest {

    @NotBlank(message = "Nome do plano é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 20, message = "Código ANS deve ter no máximo 20 caracteres")
    private String codigoAns;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    private Boolean ativo = true;

    // Construtores
    public PlanoSaudeRequest() {
    }

    public PlanoSaudeRequest(String nome, String codigoAns, String descricao) {
        this.nome = nome;
        this.codigoAns = codigoAns;
        this.descricao = descricao;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoAns() {
        return codigoAns;
    }

    public void setCodigoAns(String codigoAns) {
        this.codigoAns = codigoAns;
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
}