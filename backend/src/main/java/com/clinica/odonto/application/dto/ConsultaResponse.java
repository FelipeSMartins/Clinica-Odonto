package com.clinica.odonto.application.dto;

import com.clinica.odonto.domain.entity.Consulta;
import com.clinica.odonto.domain.entity.StatusConsulta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConsultaResponse {

    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private String pacienteCpf;
    private Long dentistaId;
    private String dentistaNome;
    private String dentistaCro;
    private LocalDateTime dataHora;
    private StatusConsulta status;
    private String procedimento;
    private String observacoes;
    private BigDecimal valor;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public ConsultaResponse() {}

    public ConsultaResponse(Consulta consulta) {
        this.id = consulta.getId();
        this.pacienteId = consulta.getPaciente().getId();
        this.pacienteNome = consulta.getPaciente().getNome();
        this.pacienteCpf = consulta.getPaciente().getCpf();
        this.dentistaId = consulta.getDentista().getId();
        this.dentistaNome = consulta.getDentista().getUsuario().getNome();
        this.dentistaCro = consulta.getDentista().getCro();
        this.dataHora = consulta.getDataHora();
        this.status = consulta.getStatus();
        this.procedimento = consulta.getProcedimento();
        this.observacoes = consulta.getObservacoes();
        this.valor = consulta.getValor();
        this.dataCriacao = consulta.getDataCriacao();
        this.dataAtualizacao = consulta.getDataAtualizacao();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }

    public String getPacienteCpf() {
        return pacienteCpf;
    }

    public void setPacienteCpf(String pacienteCpf) {
        this.pacienteCpf = pacienteCpf;
    }

    public Long getDentistaId() {
        return dentistaId;
    }

    public void setDentistaId(Long dentistaId) {
        this.dentistaId = dentistaId;
    }

    public String getDentistaNome() {
        return dentistaNome;
    }

    public void setDentistaNome(String dentistaNome) {
        this.dentistaNome = dentistaNome;
    }

    public String getDentistaCro() {
        return dentistaCro;
    }

    public void setDentistaCro(String dentistaCro) {
        this.dentistaCro = dentistaCro;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public String getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(String procedimento) {
        this.procedimento = procedimento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
}