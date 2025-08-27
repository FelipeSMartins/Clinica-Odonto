package com.clinica.odonto.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_consulta")
public class MaterialConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Material é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @NotNull(message = "Consulta é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false)
    private Consulta consulta;

    @NotNull(message = "Quantidade utilizada é obrigatória")
    @Positive(message = "Quantidade utilizada deve ser positiva")
    @Column(name = "quantidade_utilizada", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantidadeUtilizada;

    @NotNull(message = "Preço unitário é obrigatório")
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_utilizacao", nullable = false)
    private LocalDateTime dataUtilizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_lancamento_id")
    private Usuario usuarioLancamento;

    public MaterialConsulta() {
        this.dataUtilizacao = LocalDateTime.now();
    }

    public MaterialConsulta(Material material, Consulta consulta, BigDecimal quantidadeUtilizada, 
                           BigDecimal precoUnitario, Usuario usuarioLancamento) {
        this();
        this.material = material;
        this.consulta = consulta;
        this.quantidadeUtilizada = quantidadeUtilizada;
        this.precoUnitario = precoUnitario;
        this.usuarioLancamento = usuarioLancamento;
        this.valorTotal = quantidadeUtilizada.multiply(precoUnitario);
    }

    @PrePersist
    @PreUpdate
    public void calcularValorTotal() {
        if (quantidadeUtilizada != null && precoUnitario != null) {
            this.valorTotal = quantidadeUtilizada.multiply(precoUnitario);
        }
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

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public BigDecimal getQuantidadeUtilizada() {
        return quantidadeUtilizada;
    }

    public void setQuantidadeUtilizada(BigDecimal quantidadeUtilizada) {
        this.quantidadeUtilizada = quantidadeUtilizada;
        calcularValorTotal();
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        calcularValorTotal();
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataUtilizacao() {
        return dataUtilizacao;
    }

    public void setDataUtilizacao(LocalDateTime dataUtilizacao) {
        this.dataUtilizacao = dataUtilizacao;
    }

    public Usuario getUsuarioLancamento() {
        return usuarioLancamento;
    }

    public void setUsuarioLancamento(Usuario usuarioLancamento) {
        this.usuarioLancamento = usuarioLancamento;
    }
}