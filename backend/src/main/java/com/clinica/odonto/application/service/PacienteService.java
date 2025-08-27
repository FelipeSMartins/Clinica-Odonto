package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.PacienteRequest;
import com.clinica.odonto.application.dto.PacienteResponse;
import com.clinica.odonto.domain.entity.Paciente;
import com.clinica.odonto.domain.entity.PlanoSaude;
import com.clinica.odonto.domain.repository.PacienteRepository;
import com.clinica.odonto.domain.repository.PlanoSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PlanoSaudeRepository planoSaudeRepository;

    public PacienteResponse criarPaciente(PacienteRequest request) {
        // Verificar se CPF já existe
        if (pacienteRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado no sistema");
        }

        Paciente paciente = new Paciente();
        paciente.setNome(request.getNome());
        paciente.setCpf(request.getCpf());
        paciente.setDataNascimento(request.getDataNascimento());
        paciente.setSexo(request.getSexo());
        paciente.setEmail(request.getEmail());
        paciente.setTelefone(request.getTelefone());
        paciente.setCelular(request.getCelular());
        paciente.setEndereco(request.getEndereco());
        paciente.setObservacoes(request.getObservacoes());

        // Associar plano de saúde se fornecido
        if (request.getPlanoSaudeId() != null) {
            PlanoSaude planoSaude = planoSaudeRepository.findById(request.getPlanoSaudeId())
                    .orElseThrow(() -> new RuntimeException("Plano de saúde não encontrado"));
            paciente.setPlanoSaude(planoSaude);
        }

        Paciente pacienteSalvo = pacienteRepository.save(paciente);
        return converterParaResponse(pacienteSalvo);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarTodos() {
        return pacienteRepository.findAllAtivos()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<PacienteResponse> listarComPaginacao(Pageable pageable) {
        return pacienteRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public Optional<PacienteResponse> buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorNome(String nome) {
        return pacienteRepository.findByNomeContainingAndAtivo(nome)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PacienteResponse> buscarPorCpf(String cpf) {
        return pacienteRepository.findByCpf(cpf)
                .map(this::converterParaResponse);
    }

    public PacienteResponse atualizarPaciente(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        // Verificar se CPF já existe em outro paciente
        if (!paciente.getCpf().equals(request.getCpf()) && 
            pacienteRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado para outro paciente");
        }

        paciente.setNome(request.getNome());
        paciente.setCpf(request.getCpf());
        paciente.setDataNascimento(request.getDataNascimento());
        paciente.setSexo(request.getSexo());
        paciente.setEmail(request.getEmail());
        paciente.setTelefone(request.getTelefone());
        paciente.setCelular(request.getCelular());
        paciente.setEndereco(request.getEndereco());
        paciente.setObservacoes(request.getObservacoes());
        
        // Atualizar plano de saúde
        if (request.getPlanoSaudeId() != null) {
            PlanoSaude planoSaude = planoSaudeRepository.findById(request.getPlanoSaudeId())
                    .orElseThrow(() -> new RuntimeException("Plano de saúde não encontrado"));
            paciente.setPlanoSaude(planoSaude);
        } else {
            paciente.setPlanoSaude(null);
        }

        Paciente pacienteAtualizado = pacienteRepository.save(paciente);
        return converterParaResponse(pacienteAtualizado);
    }

    public void inativarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        
        paciente.setAtivo(false);
        pacienteRepository.save(paciente);
    }

    public void ativarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        
        paciente.setAtivo(true);
        pacienteRepository.save(paciente);
    }

    private PacienteResponse converterParaResponse(Paciente paciente) {
        PacienteResponse response = new PacienteResponse();
        response.setId(paciente.getId());
        response.setNome(paciente.getNome());
        response.setCpf(paciente.getCpf());
        response.setDataNascimento(paciente.getDataNascimento());
        response.setSexo(paciente.getSexo());
        response.setEmail(paciente.getEmail());
        response.setTelefone(paciente.getTelefone());
        response.setCelular(paciente.getCelular());
        response.setEndereco(paciente.getEndereco());
        response.setObservacoes(paciente.getObservacoes());
        response.setAtivo(paciente.getAtivo());
        response.setDataCadastro(paciente.getDataCadastro());
        response.setDataAtualizacao(paciente.getDataAtualizacao());
        
        // Informações do plano de saúde
        if (paciente.getPlanoSaude() != null) {
            response.setPlanoSaudeId(paciente.getPlanoSaude().getId());
            response.setPlanoSaudeNome(paciente.getPlanoSaude().getNome());
        }
        
        return response;
    }
}