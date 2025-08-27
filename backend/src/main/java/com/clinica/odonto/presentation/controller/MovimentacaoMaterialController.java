package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.MovimentacaoMaterialRequest;
import com.clinica.odonto.application.dto.MovimentacaoMaterialResponse;
import com.clinica.odonto.application.service.MovimentacaoMaterialService;
import com.clinica.odonto.domain.entity.TipoMovimentacao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimentacoes-material")
@CrossOrigin(origins = "*")
public class MovimentacaoMaterialController {

    @Autowired
    private MovimentacaoMaterialService movimentacaoMaterialService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MovimentacaoMaterialResponse> registrarMovimentacao(
            @Valid @RequestBody MovimentacaoMaterialRequest request) {
        try {
            // Obter ID do usuário logado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long usuarioId = Long.parseLong(authentication.getName());
            
            MovimentacaoMaterialResponse response = movimentacaoMaterialService.registrarMovimentacao(request, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MovimentacaoMaterialResponse>> listarTodas() {
        try {
            List<MovimentacaoMaterialResponse> movimentacoes = movimentacaoMaterialService.listarTodas();
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Page<MovimentacaoMaterialResponse>> listarComPaginacao(Pageable pageable) {
        try {
            Page<MovimentacaoMaterialResponse> movimentacoes = movimentacaoMaterialService.listarComPaginacao(pageable);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MovimentacaoMaterialResponse> buscarPorId(@PathVariable Long id) {
        try {
            Optional<MovimentacaoMaterialResponse> movimentacao = movimentacaoMaterialService.buscarPorId(id);
            return movimentacao.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/material/{materialId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MovimentacaoMaterialResponse>> buscarPorMaterial(@PathVariable Long materialId) {
        try {
            List<MovimentacaoMaterialResponse> movimentacoes = movimentacaoMaterialService.buscarPorMaterial(materialId);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MovimentacaoMaterialResponse>> buscarPorTipoMovimentacao(@PathVariable TipoMovimentacao tipo) {
        try {
            List<MovimentacaoMaterialResponse> movimentacoes = movimentacaoMaterialService.buscarPorTipoMovimentacao(tipo);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MovimentacaoMaterialResponse>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            LocalDateTime inicioDateTime = dataInicio.atStartOfDay();
            LocalDateTime fimDateTime = dataFim.atTime(23, 59, 59);
            List<MovimentacaoMaterialResponse> movimentacoes = movimentacaoMaterialService.buscarPorPeriodo(inicioDateTime, fimDateTime);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/material/{materialId}/periodo")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MovimentacaoMaterialResponse>> buscarPorMaterialEPeriodo(
            @PathVariable Long materialId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            LocalDateTime inicioDateTime = dataInicio.atStartOfDay();
            LocalDateTime fimDateTime = dataFim.atTime(23, 59, 59);
            List<MovimentacaoMaterialResponse> movimentacoes = movimentacaoMaterialService.buscarPorMaterialEPeriodo(materialId, inicioDateTime, fimDateTime);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/consulta/{consultaId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MovimentacaoMaterialResponse>> buscarPorConsulta(@PathVariable Long consultaId) {
        try {
            List<MovimentacaoMaterialResponse> movimentacoes = movimentacaoMaterialService.buscarPorConsulta(consultaId);
            return ResponseEntity.ok(movimentacoes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estatisticas/mes-atual")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Long> contarMovimentacoesMesAtual() {
        try {
            Long count = movimentacaoMaterialService.contarMovimentacoesMesAtual();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tipos")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<TipoMovimentacao[]> listarTiposMovimentacao() {
        try {
            return ResponseEntity.ok(TipoMovimentacao.values());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}