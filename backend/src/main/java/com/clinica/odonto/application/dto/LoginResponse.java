package com.clinica.odonto.application.dto;

import java.util.List;

public class LoginResponse {

    private String token;
    private Long id;
    private String nome;
    private String email;
    private String tipo;
    private List<String> authorities;

    public LoginResponse() {}

    public LoginResponse(String token, Long id, String nome, String email, String tipo, List<String> authorities) {
        this.token = token;
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}