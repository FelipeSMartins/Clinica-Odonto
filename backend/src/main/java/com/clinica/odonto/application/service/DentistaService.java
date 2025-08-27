package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.DentistaRequest;
import com.clinica.odonto.application.dto.DentistaResponse;
import com.clinica.odonto.application.dto.PlanoSaudeResponse;
import com.clinica.odonto.domain.entity.Dentista;
import com.clinica.odonto.domain.entity.PlanoSaude;
import com.clinica.odonto.domain.entity.TipoUsuario;
import com.clinica.odonto.domain.entity.Usuario;
import com.clinica.odonto.domain.repository.DentistaRepository;
import com.clinica.odonto.domain.repository.PlanoSaudeRepository;
import com.clinica.odonto.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DentistaService {

    @Autowired
    private DentistaRepository dentistaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PlanoSaudeRepository planoSaudeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DentistaResponse criar(DentistaRequest request) {
        // Verificar se CRO já existe
        if (dentistaRepository.existsByCro(request.getCro())) {
            throw new RuntimeException("CRO já cadastrado no sistema");
        }

        // Verificar se email já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado no sistema");
        }

        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipo(TipoUsuario.DENTISTA);
        usuario.setAtivo(true);
        
        usuario = usuarioRepository.save(usuario);

        // Criar dentista
        Dentista dentista = new Dentista();
        dentista.setUsuario(usuario);
        dentista.setCro(request.getCro());
        dentista.setEspecialidade(request.getEspecialidade());
        dentista.setTelefone(request.getTelefone());
        dentista.setAtivo(true);
        
        // Associar planos de saúde aceitos
        if (request.getPlanosAceitosIds() != null && !request.getPlanosAceitosIds().isEmpty()) {
            List<PlanoSaude> planosAceitos = planoSaudeRepository.findAllById(request.getPlanosAceitosIds());
            dentista.setPlanosAceitos(planosAceitos);
        }

        dentista = dentistaRepository.save(dentista);

        return converterParaResponse(dentista);
    }

    @Transactional(readOnly = true)
    public List<DentistaResponse> listarTodos() {
        return dentistaRepository.findAllAtivos()
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DentistaResponse> listarComPaginacao(Pageable pageable) {
        return dentistaRepository.findAll(pageable)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public Optional<DentistaResponse> buscarPorId(Long id) {
        return dentistaRepository.findById(id)
                .map(this::converterParaResponse);
    }

    @Transactional(readOnly = true)
    public List<DentistaResponse> buscarPorNome(String nome) {
        return dentistaRepository.findByNomeContainingAndAtivo(nome)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DentistaResponse> buscarPorEspecialidade(String especialidade) {
        return dentistaRepository.findByEspecialidadeContainingAndAtivo(especialidade)
                .stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<DentistaResponse> buscarPorCro(String cro) {
        return dentistaRepository.findByCro(cro)
                .map(this::converterParaResponse);
    }

    public DentistaResponse atualizar(Long id, DentistaRequest request) {
        Dentista dentista = dentistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));

        // Verificar se CRO já existe (exceto para o próprio dentista)
        if (!dentista.getCro().equals(request.getCro()) && 
            dentistaRepository.existsByCro(request.getCro())) {
            throw new RuntimeException("CRO já cadastrado no sistema");
        }

        // Atualizar dados do usuário
        Usuario usuario = dentista.getUsuario();
        usuario.setNome(request.getNome());
        
        // Verificar se email já existe (exceto para o próprio usuário)
        if (!usuario.getEmail().equals(request.getEmail()) && 
            usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado no sistema");
        }
        
        usuario.setEmail(request.getEmail());
        
        // Atualizar senha apenas se fornecida
        if (request.getSenha() != null && !request.getSenha().trim().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }
        
        usuarioRepository.save(usuario);

        // Atualizar dados do dentista
        dentista.setCro(request.getCro());
        dentista.setEspecialidade(request.getEspecialidade());
        dentista.setTelefone(request.getTelefone());
        
        // Atualizar planos de saúde aceitos
        if (request.getPlanosAceitosIds() != null) {
            if (request.getPlanosAceitosIds().isEmpty()) {
                dentista.getPlanosAceitos().clear();
            } else {
                List<PlanoSaude> planosAceitos = planoSaudeRepository.findAllById(request.getPlanosAceitosIds());
                dentista.setPlanosAceitos(planosAceitos);
            }
        }

        dentista = dentistaRepository.save(dentista);

        return converterParaResponse(dentista);
    }

    public void ativar(Long id) {
        Dentista dentista = dentistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
        
        dentista.setAtivo(true);
        dentista.getUsuario().setAtivo(true);
        
        usuarioRepository.save(dentista.getUsuario());
        dentistaRepository.save(dentista);
    }

    public void inativar(Long id) {
        Dentista dentista = dentistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
        
        dentista.setAtivo(false);
        dentista.getUsuario().setAtivo(false);
        
        usuarioRepository.save(dentista.getUsuario());
        dentistaRepository.save(dentista);
    }
    
    private DentistaResponse converterParaResponse(Dentista dentista) {
        DentistaResponse response = new DentistaResponse(dentista);
        
        // Adicionar informações dos planos aceitos
        if (dentista.getPlanosAceitos() != null && !dentista.getPlanosAceitos().isEmpty()) {
            List<PlanoSaudeResponse> planosResponse = dentista.getPlanosAceitos().stream()
                    .map(plano -> new PlanoSaudeResponse(plano))
                    .collect(Collectors.toList());
            response.setPlanosAceitos(planosResponse);
        }
        
        return response;
    }
}