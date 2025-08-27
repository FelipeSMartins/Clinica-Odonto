package com.clinica.odonto.application.dto;

import java.math.BigDecimal;

public class DashboardMetricsResponse {

    private Long totalPacientes;
    private Long pacientesCadastradosNoMes;
    private Long totalDentistas;
    private Long consultasHoje;
    private Long consultasAgendadasHoje;
    private Long consultasConcluidasHoje;
    private Long consultasNoMes;
    private BigDecimal faturamentoMensal;
    private Long totalUsuarios;

    public DashboardMetricsResponse() {}

    // Getters and Setters
    public Long getTotalPacientes() {
        return totalPacientes;
    }

    public void setTotalPacientes(Long totalPacientes) {
        this.totalPacientes = totalPacientes;
    }

    public Long getPacientesCadastradosNoMes() {
        return pacientesCadastradosNoMes;
    }

    public void setPacientesCadastradosNoMes(Long pacientesCadastradosNoMes) {
        this.pacientesCadastradosNoMes = pacientesCadastradosNoMes;
    }

    public Long getTotalDentistas() {
        return totalDentistas;
    }

    public void setTotalDentistas(Long totalDentistas) {
        this.totalDentistas = totalDentistas;
    }

    public Long getConsultasHoje() {
        return consultasHoje;
    }

    public void setConsultasHoje(Long consultasHoje) {
        this.consultasHoje = consultasHoje;
    }

    public Long getConsultasAgendadasHoje() {
        return consultasAgendadasHoje;
    }

    public void setConsultasAgendadasHoje(Long consultasAgendadasHoje) {
        this.consultasAgendadasHoje = consultasAgendadasHoje;
    }

    public Long getConsultasConcluidasHoje() {
        return consultasConcluidasHoje;
    }

    public void setConsultasConcluidasHoje(Long consultasConcluidasHoje) {
        this.consultasConcluidasHoje = consultasConcluidasHoje;
    }

    public Long getConsultasNoMes() {
        return consultasNoMes;
    }

    public void setConsultasNoMes(Long consultasNoMes) {
        this.consultasNoMes = consultasNoMes;
    }

    public BigDecimal getFaturamentoMensal() {
        return faturamentoMensal;
    }

    public void setFaturamentoMensal(BigDecimal faturamentoMensal) {
        this.faturamentoMensal = faturamentoMensal;
    }

    public Long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(Long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }
}