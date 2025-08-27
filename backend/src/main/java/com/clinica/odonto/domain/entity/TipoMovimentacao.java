package com.clinica.odonto.domain.entity;

public enum TipoMovimentacao {
    ENTRADA("Entrada"),
    SAIDA("Saída"),
    AJUSTE_POSITIVO("Ajuste Positivo"),
    AJUSTE_NEGATIVO("Ajuste Negativo"),
    USO_CONSULTA("Uso em Consulta"),
    PERDA("Perda"),
    VENCIMENTO("Vencimento");

    private final String descricao;

    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}