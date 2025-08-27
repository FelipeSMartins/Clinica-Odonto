package com.clinica.odonto.presentation.controller;

import com.clinica.odonto.application.dto.LoginRequest;
import com.clinica.odonto.application.dto.LoginResponse;
import com.clinica.odonto.application.dto.UsuarioRequest;
import com.clinica.odonto.application.service.AuthService;
import com.clinica.odonto.domain.entity.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao fazer login: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        try {
            Usuario usuario = authService.registrarUsuario(usuarioRequest);
            return ResponseEntity.ok("Usuário registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            // Implementar lógica para obter usuário atual baseado no token
            return ResponseEntity.ok("Usuário autenticado");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao obter usuário: " + e.getMessage());
        }
    }
}