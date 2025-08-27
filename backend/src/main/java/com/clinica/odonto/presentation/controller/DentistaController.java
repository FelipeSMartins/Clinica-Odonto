package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.DentistaRequest;
import com.clinica.odonto.application.dto.DentistaResponse;
import com.clinica.odonto.application.service.DentistaService;
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
@RequestMapping("/api/dentistas")
@CrossOrigin(origins = "*")
public class DentistaController {

    @Autowired
    private DentistaService dentistaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<DentistaResponse> criar(@Valid @RequestBody DentistaRequest request) {
        try {
            DentistaResponse dentista = dentistaService.criar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(dentista);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<DentistaResponse>> listarTodos() {
        try {
            List<DentistaResponse> dentistas = dentistaService.listarTodos();
            return ResponseEntity.ok(dentistas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/paginado")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<Page<DentistaResponse>> listarComPaginacao(Pageable pageable) {
        try {
            Page<DentistaResponse> dentistas = dentistaService.listarComPaginacao(pageable);
            return ResponseEntity.ok(dentistas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<DentistaResponse> buscarPorId(@PathVariable Long id) {
        try {
            Optional<DentistaResponse> dentista = dentistaService.buscarPorId(id);
            return dentista.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar/nome")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<DentistaResponse>> buscarPorNome(@RequestParam String nome) {
        try {
            List<DentistaResponse> dentistas = dentistaService.buscarPorNome(nome);
            return ResponseEntity.ok(dentistas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar/especialidade")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<List<DentistaResponse>> buscarPorEspecialidade(@RequestParam String especialidade) {
        try {
            List<DentistaResponse> dentistas = dentistaService.buscarPorEspecialidade(especialidade);
            return ResponseEntity.ok(dentistas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/buscar/cro")
    @PreAuthorize("hasAnyRole('ADMIN', 'DENTISTA', 'RECEPCIONISTA')")
    public ResponseEntity<DentistaResponse> buscarPorCro(@RequestParam String cro) {
        try {
            Optional<DentistaResponse> dentista = dentistaService.buscarPorCro(cro);
            return dentista.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<DentistaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody DentistaRequest request) {
        try {
            DentistaResponse dentista = dentistaService.atualizar(id, request);
            return ResponseEntity.ok(dentista);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{id}/ativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        try {
            dentistaService.ativar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        try {
            dentistaService.inativar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}