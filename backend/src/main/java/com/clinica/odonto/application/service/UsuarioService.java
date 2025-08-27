package com.clinica.odonto.application.service;

import com.clinica.odonto.application.dto.UsuarioRequest;
import com.clinica.odonto.application.dto.UsuarioResponse;
import com.clinica.odonto.domain.entity.Usuario;
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
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioResponse criarUsuario(UsuarioRequest request) {
        // Verificar se email já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado no sistema");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipo(request.getTipo());
        usuario.setAtivo(true);

        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarComPaginacao(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(UsuarioResponse::new);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponse> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponse::new);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarPorNome(String nome) {
        return usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getNome().toLowerCase().contains(nome.toLowerCase()))
                .map(UsuarioResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioResponse> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(UsuarioResponse::new);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarAtivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(Usuario::getAtivo)
                .map(UsuarioResponse::new)
                .collect(Collectors.toList());
    }

    public UsuarioResponse atualizarUsuario(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verificar se email já existe (exceto para o próprio usuário)
        if (!usuario.getEmail().equals(request.getEmail()) && 
            usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado no sistema");
        }

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setTipo(request.getTipo());
        
        // Atualizar senha apenas se fornecida
        if (request.getSenha() != null && !request.getSenha().trim().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario);
    }

    public UsuarioResponse ativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        usuario.setAtivo(true);
        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario);
    }

    public UsuarioResponse desativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Não permitir desativar o próprio usuário se for o único admin
        if (usuario.getTipo().name().equals("ADMIN")) {
            long totalAdmins = usuarioRepository.findAll()
                    .stream()
                    .filter(u -> u.getTipo().name().equals("ADMIN") && u.getAtivo())
                    .count();
            
            if (totalAdmins <= 1) {
                throw new RuntimeException("Não é possível desativar o último administrador do sistema");
            }
        }
        
        usuario.setAtivo(false);
        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario);
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Não permitir deletar o próprio usuário se for o único admin
        if (usuario.getTipo().name().equals("ADMIN")) {
            long totalAdmins = usuarioRepository.findAll()
                    .stream()
                    .filter(u -> u.getTipo().name().equals("ADMIN") && u.getAtivo())
                    .count();
            
            if (totalAdmins <= 1) {
                throw new RuntimeException("Não é possível deletar o último administrador do sistema");
            }
        }
        
        usuarioRepository.delete(usuario);
    }

    public UsuarioResponse alterarSenha(Long id, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario = usuarioRepository.save(usuario);
        return new UsuarioResponse(usuario);
    }
}