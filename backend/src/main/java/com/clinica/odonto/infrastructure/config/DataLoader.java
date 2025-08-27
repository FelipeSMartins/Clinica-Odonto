package com.clinica.odonto.infrastructure.config;

import com.clinica.odonto.domain.entity.TipoUsuario;
import com.clinica.odonto.domain.entity.Usuario;
import com.clinica.odonto.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem usuários no banco
        if (usuarioRepository.count() == 0) {
            criarUsuariosPadrao();
        }
    }

    private void criarUsuariosPadrao() {
        // Criar usuário Admin
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail("admin@clinica.com");
        admin.setSenha(passwordEncoder.encode("admin123"));
        admin.setTipo(TipoUsuario.ADMIN);
        admin.setAtivo(true);
        usuarioRepository.save(admin);

        // Criar usuário Dentista
        Usuario dentista = new Usuario();
        dentista.setNome("Dr. João Silva");
        dentista.setEmail("dentista@clinica.com");
        dentista.setSenha(passwordEncoder.encode("dentista123"));
        dentista.setTipo(TipoUsuario.DENTISTA);
        dentista.setAtivo(true);
        usuarioRepository.save(dentista);

        // Criar usuário Recepcionista
        Usuario recepcionista = new Usuario();
        recepcionista.setNome("Maria Santos");
        recepcionista.setEmail("recepcao@clinica.com");
        recepcionista.setSenha(passwordEncoder.encode("recepcao123"));
        recepcionista.setTipo(TipoUsuario.RECEPCIONISTA);
        recepcionista.setAtivo(true);
        usuarioRepository.save(recepcionista);

        System.out.println("Usuários padrão criados com sucesso!");
        System.out.println("Admin: admin@clinica.com / admin123");
        System.out.println("Dentista: dentista@clinica.com / dentista123");
        System.out.println("Recepcionista: recepcao@clinica.com / recepcao123");
    }
}