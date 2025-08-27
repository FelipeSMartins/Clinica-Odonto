package com.clinica.odonto.domain.entity;

public enum TipoUsuario {
    ADMIN("Administrador"),
    DENTISTA("Dentista"),
    RECEPCIONISTA("Recepcionista"),
    ASSISTENTE("Assistente");

    private final String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}