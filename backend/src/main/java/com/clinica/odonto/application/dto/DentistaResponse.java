package com.clinica.odonto.application.dto;

import com.clinica.odonto.domain.entity.Dentista;
import com.clinica.odonto.domain.entity.TipoUsuario;

import java.time.LocalDateTime;
import java.util.List;

public class DentistaResponse {

    private Long id;
    private Long usuarioId;
    private String nome;
    private String email;
    private TipoUsuario tipo;
    private String cro;
    private String especialidade;
    private String telefone;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    private List<PlanoSaudeResponse> planosAceitos;

    public DentistaResponse() {}

    public DentistaResponse(Dentista dentista) {
        this.id = dentista.getId();
        this.usuarioId = dentista.getUsuario().getId();
        this.nome = dentista.getUsuario().getNome();
        this.email = dentista.getUsuario().getEmail();
        this.tipo = dentista.getUsuario().getTipo();
        this.cro = dentista.getCro();
        this.especialidade = dentista.getEspecialidade();
        this.telefone = dentista.getTelefone();
        this.ativo = dentista.getAtivo();
        this.dataCadastro = dentista.getDataCadastro();
        this.dataAtualizacao = dentista.getDataAtualizacao();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
    
    public List<PlanoSaudeResponse> getPlanosAceitos() {
        return planosAceitos;
    }
    
    public void setPlanosAceitos(List<PlanoSaudeResponse> planosAceitos) {
        this.planosAceitos = planosAceitos;
    }
}