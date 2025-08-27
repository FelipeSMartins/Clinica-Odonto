package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.MaterialRequest;
import com.clinica.odonto.application.dto.MaterialResponse;
import com.clinica.odonto.application.service.MaterialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/materiais")
@CrossOrigin(origins = "*")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MaterialResponse> criar(@Valid @RequestBody MaterialRequest request) {
        try {
            MaterialResponse response = materialService.criarMaterial(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialResponse>> listarTodos() {
        try {
            List<MaterialResponse> materiais = materialService.listarTodos();
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Page<MaterialResponse>> listarPaginado(Pageable pageable) {
        try {
            Page<MaterialResponse> materiais = materialService.listarComPaginacao(pageable);
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MaterialResponse> buscarPorId(@PathVariable Long id) {
        try {
            Optional<MaterialResponse> material = materialService.buscarPorId(id);
            return material.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialResponse>> buscarPorNome(@RequestParam String nome) {
        try {
            List<MaterialResponse> materiais = materialService.buscarPorNome(nome);
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/codigo/{codigo}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MaterialResponse> buscarPorCodigo(@PathVariable String codigo) {
        try {
            Optional<MaterialResponse> material = materialService.buscarPorCodigo(codigo);
            return material.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categoria")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialResponse>> buscarPorCategoria(@RequestParam String categoria) {
        try {
            List<MaterialResponse> materiais = materialService.buscarPorCategoria(categoria);
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estoque-baixo")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialResponse>> buscarMateriaisComEstoqueBaixo() {
        try {
            List<MaterialResponse> materiais = materialService.buscarMateriaisComEstoqueBaixo();
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/categorias")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<String>> listarCategorias() {
        try {
            List<String> categorias = materialService.listarCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/pesquisar")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DENTISTA', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<List<MaterialResponse>> pesquisarMateriais(@RequestParam String termo) {
        try {
            List<MaterialResponse> materiais = materialService.pesquisarMateriais(termo);
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<MaterialResponse> atualizarMaterial(
            @PathVariable Long id, 
            @Valid @RequestBody MaterialRequest request) {
        try {
            MaterialResponse response = materialService.atualizarMaterial(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estoque")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Void> atualizarEstoque(
            @PathVariable Long id, 
            @RequestParam BigDecimal novoEstoque) {
        try {
            materialService.atualizarEstoque(id, novoEstoque);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Void> inativarMaterial(@PathVariable Long id) {
        try {
            materialService.inativarMaterial(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Void> ativarMaterial(@PathVariable Long id) {
        try {
            materialService.ativarMaterial(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estatisticas/ativos")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Long> contarMateriaisAtivos() {
        try {
            Long count = materialService.contarMateriaisAtivos();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estatisticas/estoque-baixo")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_RECEPCIONISTA')")
    public ResponseEntity<Long> contarMateriaisComEstoqueBaixo() {
        try {
            Long count = materialService.contarMateriaisComEstoqueBaixo();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}