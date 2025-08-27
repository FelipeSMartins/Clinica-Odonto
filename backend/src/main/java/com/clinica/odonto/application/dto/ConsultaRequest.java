package com.clinica.odonto.application.dto;

import com.clinica.odonto.domain.entity.StatusConsulta;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConsultaRequest {

    @NotNull(message = "ID do paciente é obrigatório")
    private Long pacienteId;

    @NotNull(message = "ID do dentista é obrigatório")
    private Long dentistaId;

    @NotNull(message = "Data e hora são obrigatórias")
    @Future(message = "Data e hora devem ser futuras")
    private LocalDateTime dataHora;

    @Size(max = 200, message = "Procedimento deve ter no máximo 200 caracteres")
    private String procedimento;

    private String observacoes;

    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    private StatusConsulta status;

    public ConsultaRequest() {}

    public ConsultaRequest(Long pacienteId, Long dentistaId, LocalDateTime dataHora, String procedimento) {
        this.pacienteId = pacienteId;
        this.dentistaId = dentistaId;
        this.dataHora = dataHora;
        this.procedimento = procedimento;
        this.status = StatusConsulta.AGENDADA;
    }

    // Getters and Setters
    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getDentistaId() {
        return dentistaId;
    }

    public void setDentistaId(Long dentistaId) {
        this.dentistaId = dentistaId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
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

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }
}