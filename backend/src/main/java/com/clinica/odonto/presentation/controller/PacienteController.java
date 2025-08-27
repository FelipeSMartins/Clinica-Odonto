package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.PacienteRequest;
import com.clinica.odonto.application.dto.PacienteResponse;
import com.clinica.odonto.application.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<PacienteResponse> criarPaciente(@Valid @RequestBody PacienteRequest request) {
        try {
            PacienteResponse response = pacienteService.criarPaciente(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<PacienteResponse>> listarTodos() {
        try {
            List<PacienteResponse> pacientes = pacienteService.listarTodos();
            return ResponseEntity.ok(pacientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<Page<PacienteResponse>> listarComPaginacao(Pageable pageable) {
        try {
            Page<PacienteResponse> pacientes = pacienteService.listarComPaginacao(pageable);
            return ResponseEntity.ok(pacientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<PacienteResponse> buscarPorId(@PathVariable Long id) {
        try {
            Optional<PacienteResponse> paciente = pacienteService.buscarPorId(id);
            return paciente.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<PacienteResponse>> buscarPorNome(@RequestParam String nome) {
        try {
            List<PacienteResponse> pacientes = pacienteService.buscarPorNome(nome);
            return ResponseEntity.ok(pacientes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<PacienteResponse> buscarPorCpf(@PathVariable String cpf) {
        try {
            Optional<PacienteResponse> paciente = pacienteService.buscarPorCpf(cpf);
            return paciente.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<PacienteResponse> atualizarPaciente(
            @PathVariable Long id, 
            @Valid @RequestBody PacienteRequest request) {
        try {
            PacienteResponse response = pacienteService.atualizarPaciente(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> inativarPaciente(@PathVariable Long id) {
        try {
            pacienteService.inativarPaciente(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> ativarPaciente(@PathVariable Long id) {
        try {
            pacienteService.ativarPaciente(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}