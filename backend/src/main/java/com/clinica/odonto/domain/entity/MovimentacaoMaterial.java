package com.clinica.odonto.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacoes_material")
public class MovimentacaoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Material é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @NotNull(message = "Tipo de movimentação é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidade;

    @Column(name = "estoque_anterior", precision = 10, scale = 2)
    private BigDecimal estoqueAnterior;

    @Column(name = "estoque_atual", precision = 10, scale = 2)
    private BigDecimal estoqueAtual;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @NotNull(message = "Data da movimentação é obrigatória")
    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    public MovimentacaoMaterial() {
        this.dataMovimentacao = LocalDateTime.now();
    }

    public MovimentacaoMaterial(Material material, TipoMovimentacao tipoMovimentacao, 
                               BigDecimal quantidade, BigDecimal estoqueAnterior, 
                               BigDecimal estoqueAtual, Usuario usuario) {
        this();
        this.material = material;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
        this.estoqueAnterior = estoqueAnterior;
        this.estoqueAtual = estoqueAtual;
        this.usuario = usuario;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getEstoqueAnterior() {
        return estoqueAnterior;
    }

    public void setEstoqueAnterior(BigDecimal estoqueAnterior) {
        this.estoqueAnterior = estoqueAnterior;
    }

    public BigDecimal getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(BigDecimal estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }
}