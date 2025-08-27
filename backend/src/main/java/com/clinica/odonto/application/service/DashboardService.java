package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.DashboardMetricsResponse;
import com.clinica.odonto.domain.entity.StatusConsulta;
import com.clinica.odonto.domain.repository.ConsultaRepository;
import com.clinica.odonto.domain.repository.DentistaRepository;
import com.clinica.odonto.domain.repository.PacienteRepository;
import com.clinica.odonto.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private DentistaRepository dentistaRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DashboardMetricsResponse obterMetricas() {
        DashboardMetricsResponse metrics = new DashboardMetricsResponse();

        // Métricas de pacientes
        metrics.setTotalPacientes(pacienteRepository.countPacientesAtivos());
        metrics.setPacientesCadastradosNoMes(pacienteRepository.countPacientesCadastradosNoMes());

        // Métricas de dentistas
        metrics.setTotalDentistas(dentistaRepository.countDentistasAtivos());

        // Métricas de consultas
        metrics.setConsultasHoje(consultaRepository.countConsultasHoje());
        metrics.setConsultasAgendadasHoje(consultaRepository.countConsultasHojePorStatus(StatusConsulta.AGENDADA));
        metrics.setConsultasConcluidasHoje(consultaRepository.countConsultasHojePorStatus(StatusConsulta.CONCLUIDA));
        metrics.setConsultasNoMes(consultaRepository.countConsultasNoMes());

        // Métricas financeiras
        BigDecimal faturamentoMensal = consultaRepository.somaFaturamentoMensal();
        metrics.setFaturamentoMensal(faturamentoMensal != null ? faturamentoMensal : BigDecimal.ZERO);

        // Métricas de usuários
        metrics.setTotalUsuarios(usuarioRepository.countUsuariosAtivos());

        return metrics;
    }
}