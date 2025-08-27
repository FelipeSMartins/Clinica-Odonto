package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.PlanoSaudeRequest;
import com.clinica.odonto.application.dto.PlanoSaudeResponse;
import com.clinica.odonto.domain.entity.PlanoSaude;
import com.clinica.odonto.domain.repository.PlanoSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlanoSaudeService {

    @Autowired
    private PlanoSaudeRepository planoSaudeRepository;

    public PlanoSaudeResponse criarPlanoSaude(PlanoSaudeRequest request) {
        // Verificar se código ANS já existe (se fornecido)
        if (request.getCodigoAns() != null && !request.getCodigoAns().trim().isEmpty()) {
            if (planoSaudeRepository.existsByCodigoAns(request.getCodigoAns())) {
                throw new RuntimeException("Código ANS já cadastrado no sistema");
            }
        }

        PlanoSaude planoSaude = new PlanoSaude();
        planoSaude.setNome(request.getNome());
        planoSaude.setCodigoAns(request.getCodigoAns());
        planoSaude.setDescricao(request.getDescricao());
        planoSaude.setAtivo(request.getAtivo());

        PlanoSaude planoSalvo = planoSaudeRepository.save(planoSaude);
        return converterParaResponse(planoSalvo);
    }

    @Transactional(readOnly = true)
    public List<PlanoSaudeResponse> listarTodos() {
        return planoSaudeRepository.findAllAtivos()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlanoSaudeResponse> listarTodosIncluindoInativos() {
        return planoSaudeRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<PlanoSaudeResponse> buscarPorId(Long id) {
        return planoSaudeRepository.findById(id)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public List<PlanoSaudeResponse> buscarPorNome(String nome) {
        return planoSaudeRepository.findByNomeContainingAndAtivo(nome)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlanoSaudeResponse> buscarPorDentista(Long dentistaId) {
        return planoSaudeRepository.findByDentistaId(dentistaId)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    public PlanoSaudeResponse atualizarPlanoSaude(Long id, PlanoSaudeRequest request) {
        PlanoSaude planoSaude = planoSaudeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano de saúde não encontrado"));

        // Verificar se código ANS já existe em outro plano (se fornecido)
        if (request.getCodigoAns() != null && !request.getCodigoAns().trim().isEmpty()) {
            Optional<PlanoSaude> planoExistente = planoSaudeRepository.findByCodigoAns(request.getCodigoAns());
            if (planoExistente.isPresent() && !planoExistente.get().getId().equals(id)) {
                throw new RuntimeException("Código ANS já cadastrado em outro plano");
            }
        }

        planoSaude.setNome(request.getNome());
        planoSaude.setCodigoAns(request.getCodigoAns());
        planoSaude.setDescricao(request.getDescricao());
        planoSaude.setAtivo(request.getAtivo());

        PlanoSaude planoAtualizado = planoSaudeRepository.save(planoSaude);
        return converterParaResponse(planoAtualizado);
    }

    public void deletarPlanoSaude(Long id) {
        PlanoSaude planoSaude = planoSaudeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano de saúde não encontrado"));

        // Verificar se há pacientes ou dentistas associados
        if (!planoSaude.getPacientes().isEmpty()) {
            throw new RuntimeException("Não é possível excluir plano com pacientes associados. Desative o plano ao invés de excluir.");
        }

        if (!planoSaude.getDentistas().isEmpty()) {
            throw new RuntimeException("Não é possível excluir plano com dentistas associados. Desative o plano ao invés de excluir.");
        }

        planoSaudeRepository.delete(planoSaude);
    }

    public PlanoSaudeResponse ativarDesativarPlano(Long id) {
        PlanoSaude planoSaude = planoSaudeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano de saúde não encontrado"));

        planoSaude.setAtivo(!planoSaude.getAtivo());
        PlanoSaude planoAtualizado = planoSaudeRepository.save(planoSaude);
        return converterParaResponse(planoAtualizado);
    }

    @Transactional(readOnly = true)
    public Long contarPlanosAtivos() {
        return planoSaudeRepository.countPlanosAtivos();
    }

    private PlanoSaudeResponse converterParaResponse(PlanoSaude planoSaude) {
        PlanoSaudeResponse response = new PlanoSaudeResponse();
        response.setId(planoSaude.getId());
        response.setNome(planoSaude.getNome());
        response.setCodigoAns(planoSaude.getCodigoAns());
        response.setDescricao(planoSaude.getDescricao());
        response.setAtivo(planoSaude.getAtivo());
        response.setDataCriacao(planoSaude.getDataCriacao());
        response.setDataAtualizacao(planoSaude.getDataAtualizacao());
        response.setQuantidadePacientes((long) planoSaude.getPacientes().size());
        response.setQuantidadeDentistas((long) planoSaude.getDentistas().size());
        return response;
    }
}