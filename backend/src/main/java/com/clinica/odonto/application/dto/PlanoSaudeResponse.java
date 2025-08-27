package com.clinica.odonto.application.dto;

import java.time.LocalDateTime;
import com.clinica.odonto.domain.entity.PlanoSaude;

public class PlanoSaudeResponse {

    private Long id;
    private String nome;
    private String codigoAns;
    private String descricao;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Long quantidadePacientes;
    private Long quantidadeDentistas;

    // Construtores
    public PlanoSaudeResponse() {
    }

    public PlanoSaudeResponse(Long id, String nome, String codigoAns, String descricao, Boolean ativo, 
                             LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.codigoAns = codigoAns;
        this.descricao = descricao;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public PlanoSaudeResponse(PlanoSaude planoSaude) {
        this.id = planoSaude.getId();
        this.nome = planoSaude.getNome();
        this.codigoAns = planoSaude.getCodigoAns();
        this.descricao = planoSaude.getDescricao();
        this.ativo = planoSaude.getAtivo();
        this.dataCriacao = planoSaude.getDataCriacao();
        this.dataAtualizacao = planoSaude.getDataAtualizacao();
        this.quantidadePacientes = (long) planoSaude.getPacientes().size();
        this.quantidadeDentistas = (long) planoSaude.getDentistas().size();
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getQuantidadePacientes() {
        return quantidadePacientes;
    }

    public void setQuantidadePacientes(Long quantidadePacientes) {
        this.quantidadePacientes = quantidadePacientes;
    }

    public Long getQuantidadeDentistas() {
        return quantidadeDentistas;
    }

    public void setQuantidadeDentistas(Long quantidadeDentistas) {
        this.quantidadeDentistas = quantidadeDentistas;
    }
}