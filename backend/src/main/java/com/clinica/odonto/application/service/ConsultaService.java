package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.ConsultaRequest;
import com.clinica.odonto.application.dto.ConsultaResponse;
import com.clinica.odonto.domain.entity.Consulta;
import com.clinica.odonto.domain.entity.Dentista;
import com.clinica.odonto.domain.entity.Paciente;
import com.clinica.odonto.domain.entity.StatusConsulta;
import com.clinica.odonto.domain.repository.ConsultaRepository;
import com.clinica.odonto.domain.repository.DentistaRepository;
import com.clinica.odonto.domain.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private DentistaRepository dentistaRepository;

    public ConsultaResponse criarConsulta(ConsultaRequest request) {
        // Buscar paciente
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        if (!paciente.getAtivo()) {
            throw new RuntimeException("Paciente inativo");
        }

        // Buscar dentista
        Dentista dentista = dentistaRepository.findById(request.getDentistaId())
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));

        if (!dentista.getAtivo()) {
            throw new RuntimeException("Dentista inativo");
        }

        // Verificar se o dentista está disponível no horário
        if (verificarConflitosHorario(request.getDentistaId(), request.getDataHora())) {
            throw new RuntimeException("Dentista já possui consulta agendada neste horário");
        }

        // Criar consulta
        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setDentista(dentista);
        consulta.setDataHora(request.getDataHora());
        consulta.setProcedimento(request.getProcedimento());
        consulta.setObservacoes(request.getObservacoes());
        consulta.setValor(request.getValor());
        consulta.setStatus(request.getStatus() != null ? request.getStatus() : StatusConsulta.AGENDADA);

        consulta = consultaRepository.save(consulta);

        return new ConsultaResponse(consulta);
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> listarTodas() {
        return consultaRepository.findAll()
                .stream()
                .map(ConsultaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ConsultaResponse> listarComPaginacao(Pageable pageable) {
        return consultaRepository.findAll(pageable)
                .map(ConsultaResponse::new);
    }

    @Transactional(readOnly = true)
    public Optional<ConsultaResponse> buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .map(ConsultaResponse::new);
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> buscarPorData(LocalDate data) {
        return consultaRepository.findByData(data)
                .stream()
                .map(ConsultaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> buscarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId)
                .stream()
                .map(ConsultaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> buscarPorDentista(Long dentistaId) {
        return consultaRepository.findByDentistaId(dentistaId)
                .stream()
                .map(ConsultaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> buscarPorStatus(StatusConsulta status) {
        return consultaRepository.findByStatus(status)
                .stream()
                .map(ConsultaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> buscarConsultasHoje() {
        return consultaRepository.findConsultasHoje()
                .stream()
                .map(ConsultaResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponse> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.findByPeriodo(inicio, fim)
                .stream()
                .map(ConsultaResponse::new)
                .collect(Collectors.toList());
    }

    public ConsultaResponse atualizarConsulta(Long id, ConsultaRequest request) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        // Verificar se pode alterar (não pode alterar consultas concluídas ou canceladas)
        if (consulta.getStatus() == StatusConsulta.CONCLUIDA || consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new RuntimeException("Não é possível alterar consultas concluídas ou canceladas");
        }

        // Se mudou o dentista ou horário, verificar conflitos
        if (!consulta.getDentista().getId().equals(request.getDentistaId()) || 
            !consulta.getDataHora().equals(request.getDataHora())) {
            
            if (verificarConflitosHorario(request.getDentistaId(), request.getDataHora(), id)) {
                throw new RuntimeException("Dentista já possui consulta agendada neste horário");
            }
        }

        // Atualizar dados
        if (request.getPacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
            consulta.setPaciente(paciente);
        }

        if (request.getDentistaId() != null) {
            Dentista dentista = dentistaRepository.findById(request.getDentistaId())
                    .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
            consulta.setDentista(dentista);
        }

        if (request.getDataHora() != null) {
            consulta.setDataHora(request.getDataHora());
        }

        if (request.getProcedimento() != null) {
            consulta.setProcedimento(request.getProcedimento());
        }

        if (request.getObservacoes() != null) {
            consulta.setObservacoes(request.getObservacoes());
        }

        if (request.getValor() != null) {
            consulta.setValor(request.getValor());
        }

        if (request.getStatus() != null) {
            consulta.setStatus(request.getStatus());
        }

        consulta = consultaRepository.save(consulta);

        return new ConsultaResponse(consulta);
    }

    public ConsultaResponse alterarStatus(Long id, StatusConsulta novoStatus) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        // Validar transições de status
        if (!validarTransicaoStatus(consulta.getStatus(), novoStatus)) {
            throw new RuntimeException("Transição de status inválida");
        }

        consulta.setStatus(novoStatus);
        consulta = consultaRepository.save(consulta);

        return new ConsultaResponse(consulta);
    }

    public void cancelarConsulta(Long id) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        if (consulta.getStatus() == StatusConsulta.CONCLUIDA) {
            throw new RuntimeException("Não é possível cancelar consulta já concluída");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);
    }

    private boolean verificarConflitosHorario(Long dentistaId, LocalDateTime dataHora) {
        return verificarConflitosHorario(dentistaId, dataHora, null);
    }

    private boolean verificarConflitosHorario(Long dentistaId, LocalDateTime dataHora, Long consultaIdExcluir) {
        // Buscar consultas do dentista no mesmo dia
        LocalDate data = dataHora.toLocalDate();
        List<Consulta> consultasNoDia = consultaRepository.findByDentistaAndPeriodo(
                dentistaId, data, data);

        // Verificar se há conflito (considerando 1 hora de duração por consulta)
        LocalDateTime inicioConsulta = dataHora;
        LocalDateTime fimConsulta = dataHora.plusHours(1);

        for (Consulta consulta : consultasNoDia) {
            // Pular a própria consulta se estiver editando
            if (consultaIdExcluir != null && consulta.getId().equals(consultaIdExcluir)) {
                continue;
            }

            // Pular consultas canceladas
            if (consulta.getStatus() == StatusConsulta.CANCELADA) {
                continue;
            }

            LocalDateTime inicioExistente = consulta.getDataHora();
            LocalDateTime fimExistente = consulta.getDataHora().plusHours(1);

            // Verificar sobreposição
            if (inicioConsulta.isBefore(fimExistente) && fimConsulta.isAfter(inicioExistente)) {
                return true; // Há conflito
            }
        }

        return false; // Sem conflito
    }

    private boolean validarTransicaoStatus(StatusConsulta statusAtual, StatusConsulta novoStatus) {
        // Regras de transição de status
        switch (statusAtual) {
            case AGENDADA:
                return novoStatus == StatusConsulta.CONFIRMADA || 
                       novoStatus == StatusConsulta.CANCELADA;
            case CONFIRMADA:
                return novoStatus == StatusConsulta.EM_ANDAMENTO || 
                       novoStatus == StatusConsulta.CANCELADA;
            case EM_ANDAMENTO:
                return novoStatus == StatusConsulta.CONCLUIDA;
            case CONCLUIDA:
            case CANCELADA:
                return false; // Estados finais
            default:
                return false;
        }
    }
}