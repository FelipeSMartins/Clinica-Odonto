package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.MaterialConsultaRequest;
import com.clinica.odonto.application.dto.MaterialConsultaResponse;
import com.clinica.odonto.application.service.MaterialConsultaService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/materiais-consulta")
@CrossOrigin(origins = "*")
public class MaterialConsultaController {

    @Autowired
    private MaterialConsultaService materialConsultaService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MaterialConsultaResponse> registrarMaterialConsulta(
            @Valid @RequestBody MaterialConsultaRequest request) {
        try {
            // Obter ID do usuário logado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long usuarioId = Long.parseLong(authentication.getName());
            
            MaterialConsultaResponse response = materialConsultaService.registrarMaterialConsulta(request, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialConsultaResponse>> listarTodos() {
        try {
            List<MaterialConsultaResponse> materiaisConsulta = materialConsultaService.listarTodos();
            return ResponseEntity.ok(materiaisConsulta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Page<MaterialConsultaResponse>> listarComPaginacao(Pageable pageable) {
        try {
            Page<MaterialConsultaResponse> materiaisConsulta = materialConsultaService.listarComPaginacao(pageable);
            return ResponseEntity.ok(materiaisConsulta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MaterialConsultaResponse> buscarPorId(@PathVariable Long id) {
        try {
            Optional<MaterialConsultaResponse> materialConsulta = materialConsultaService.buscarPorId(id);
            return materialConsulta.map(ResponseEntity::ok)
                                  .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/consulta/{consultaId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialConsultaResponse>> buscarPorConsulta(@PathVariable Long consultaId) {
        try {
            List<MaterialConsultaResponse> materiaisConsulta = materialConsultaService.buscarPorConsulta(consultaId);
            return ResponseEntity.ok(materiaisConsulta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/material/{materialId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialConsultaResponse>> buscarPorMaterial(@PathVariable Long materialId) {
        try {
            List<MaterialConsultaResponse> materiaisConsulta = materialConsultaService.buscarPorMaterial(materialId);
            return ResponseEntity.ok(materiaisConsulta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/periodo")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialConsultaResponse>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            LocalDateTime inicioDateTime = dataInicio.atStartOfDay();
            LocalDateTime fimDateTime = dataFim.atTime(23, 59, 59);
            List<MaterialConsultaResponse> materiaisConsulta = materialConsultaService.buscarPorPeriodo(inicioDateTime, fimDateTime);
            return ResponseEntity.ok(materiaisConsulta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/consulta/{consultaId}/valor-total")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<BigDecimal> calcularValorTotalMateriais(@PathVariable Long consultaId) {
        try {
            BigDecimal valorTotal = materialConsultaService.calcularValorTotalMateriais(consultaId);
            return ResponseEntity.ok(valorTotal);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/material/{materialId}/quantidade-utilizada")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<BigDecimal> calcularQuantidadeTotalUtilizada(
            @PathVariable Long materialId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        try {
            LocalDateTime inicioDateTime = dataInicio.atStartOfDay();
            LocalDateTime fimDateTime = dataFim.atTime(23, 59, 59);
            BigDecimal quantidadeTotal = materialConsultaService.calcularQuantidadeTotalUtilizada(materialId, inicioDateTime, fimDateTime);
            return ResponseEntity.ok(quantidadeTotal);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estatisticas/valor-total-mes-atual")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<BigDecimal> calcularValorTotalMesAtual() {
        try {
            BigDecimal valorTotal = materialConsultaService.calcularValorTotalMesAtual();
            return ResponseEntity.ok(valorTotal);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estatisticas/count-mes-atual")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Long> contarMateriaisUtilizadosMesAtual() {
        try {
            Long count = materialConsultaService.contarMateriaisUtilizadosMesAtual();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/quantidade")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MaterialConsultaResponse> atualizarQuantidade(
            @PathVariable Long id,
            @RequestParam BigDecimal novaQuantidade) {
        try {
            // Obter ID do usuário logado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long usuarioId = Long.parseLong(authentication.getName());
            
            MaterialConsultaResponse response = materialConsultaService.atualizarQuantidade(id, novaQuantidade, usuarioId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Void> removerMaterialConsulta(@PathVariable Long id) {
        try {
            // Obter ID do usuário logado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long usuarioId = Long.parseLong(authentication.getName());
            
            materialConsultaService.removerMaterialConsulta(id, usuarioId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}