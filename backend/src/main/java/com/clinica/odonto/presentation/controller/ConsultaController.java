package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.ConsultaRequest;
import com.clinica.odonto.application.dto.ConsultaResponse;
import com.clinica.odonto.application.service.ConsultaService;
import com.clinica.odonto.domain.entity.StatusConsulta;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin(origins = "*")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ConsultaResponse> criarConsulta(@Valid @RequestBody ConsultaRequest request) {
        try {
            ConsultaResponse consulta = consultaService.criarConsulta(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(consulta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> listarTodas() {
        try {
            List<ConsultaResponse> consultas = consultaService.listarTodas();
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/paginacao")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<Page<ConsultaResponse>> listarComPaginacao(Pageable pageable) {
        try {
            Page<ConsultaResponse> consultas = consultaService.listarComPaginacao(pageable);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<ConsultaResponse> buscarPorId(@PathVariable Long id) {
        try {
            Optional<ConsultaResponse> consulta = consultaService.buscarPorId(id);
            return consulta.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/data/{data}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<ConsultaResponse> consultas = consultaService.buscarPorData(data);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> buscarPorPaciente(@PathVariable Long pacienteId) {
        try {
            List<ConsultaResponse> consultas = consultaService.buscarPorPaciente(pacienteId);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/dentista/{dentistaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> buscarPorDentista(@PathVariable Long dentistaId) {
        try {
            List<ConsultaResponse> consultas = consultaService.buscarPorDentista(dentistaId);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> buscarPorStatus(@PathVariable StatusConsulta status) {
        try {
            List<ConsultaResponse> consultas = consultaService.buscarPorStatus(status);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/hoje")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> buscarConsultasHoje() {
        try {
            List<ConsultaResponse> consultas = consultaService.buscarConsultasHoje();
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<ConsultaResponse>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        try {
            List<ConsultaResponse> consultas = consultaService.buscarPorPeriodo(inicio, fim);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ConsultaResponse> atualizarConsulta(
            @PathVariable Long id, 
            @Valid @RequestBody ConsultaRequest request) {
        try {
            ConsultaResponse consulta = consultaService.atualizarConsulta(id, request);
            return ResponseEntity.ok(consulta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<ConsultaResponse> alterarStatus(
            @PathVariable Long id, 
            @RequestParam StatusConsulta status) {
        try {
            ConsultaResponse consulta = consultaService.alterarStatus(id, status);
            return ResponseEntity.ok(consulta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id) {
        try {
            consultaService.cancelarConsulta(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}