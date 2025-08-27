package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.PlanoSaudeRequest;
import com.clinica.odonto.application.dto.PlanoSaudeResponse;
import com.clinica.odonto.application.service.PlanoSaudeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/planos-saude")
@CrossOrigin(origins = "*")
public class PlanoSaudeController {

    @Autowired
    private PlanoSaudeService planoSaudeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoSaudeResponse> criarPlanoSaude(@Valid @RequestBody PlanoSaudeRequest request) {
        try {
            PlanoSaudeResponse response = planoSaudeService.criarPlanoSaude(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCAO')")
    public ResponseEntity<List<PlanoSaudeResponse>> listarPlanos(
            @RequestParam(value = "incluirInativos", defaultValue = "false") boolean incluirInativos) {
        List<PlanoSaudeResponse> planos;
        if (incluirInativos) {
            planos = planoSaudeService.listarTodosIncluindoInativos();
        } else {
            planos = planoSaudeService.listarTodos();
        }
        return ResponseEntity.ok(planos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCAO')")
    public ResponseEntity<PlanoSaudeResponse> buscarPorId(@PathVariable Long id) {
        Optional<PlanoSaudeResponse> plano = planoSaudeService.buscarPorId(id);
        return plano.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCAO')")
    public ResponseEntity<List<PlanoSaudeResponse>> buscarPorNome(@RequestParam String nome) {
        List<PlanoSaudeResponse> planos = planoSaudeService.buscarPorNome(nome);
        return ResponseEntity.ok(planos);
    }

    @GetMapping("/dentista/{dentistaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCAO')")
    public ResponseEntity<List<PlanoSaudeResponse>> buscarPorDentista(@PathVariable Long dentistaId) {
        List<PlanoSaudeResponse> planos = planoSaudeService.buscarPorDentista(dentistaId);
        return ResponseEntity.ok(planos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoSaudeResponse> atualizarPlano(
            @PathVariable Long id, 
            @Valid @RequestBody PlanoSaudeRequest request) {
        try {
            PlanoSaudeResponse response = planoSaudeService.atualizarPlanoSaude(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarPlano(@PathVariable Long id) {
        try {
            planoSaudeService.deletarPlanoSaude(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/ativar-desativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlanoSaudeResponse> ativarDesativarPlano(@PathVariable Long id) {
        try {
            PlanoSaudeResponse response = planoSaudeService.ativarDesativarPlano(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/estatisticas/total-ativos")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCAO')")
    public ResponseEntity<Long> contarPlanosAtivos() {
        Long total = planoSaudeService.contarPlanosAtivos();
        return ResponseEntity.ok(total);
    }
}