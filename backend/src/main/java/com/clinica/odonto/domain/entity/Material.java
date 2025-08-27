package com.clinica.odonto.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "materiais")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 2, max = 20, message = "Código deve ter entre 2 e 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String categoria;

    @NotBlank(message = "Unidade de medida é obrigatória")
    @Size(max = 10, message = "Unidade de medida deve ter no máximo 10 caracteres")
    @Column(name = "unidade_medida", nullable = false, length = 10)
    private String unidadeMedida;

    @NotNull(message = "Estoque atual é obrigatório")
    @PositiveOrZero(message = "Estoque atual deve ser zero ou positivo")
    @Column(name = "estoque_atual", nullable = false, precision = 10, scale = 2)
    private BigDecimal estoqueAtual;

    @NotNull(message = "Estoque mínimo é obrigatório")
    @PositiveOrZero(message = "Estoque mínimo deve ser zero ou positivo")
    @Column(name = "estoque_minimo", nullable = false, precision = 10, scale = 2)
    private BigDecimal estoqueMinimo;

    @NotNull(message = "Preço unitário é obrigatório")
    @PositiveOrZero(message = "Preço unitário deve ser zero ou positivo")
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimentacaoMaterial> movimentacoes = new ArrayList<>();

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MaterialConsulta> materiaisConsulta = new ArrayList<>();

    public Material() {
        this.dataCadastro = LocalDateTime.now();
    }

    public Material(String nome, String codigo, String categoria, String unidadeMedida, 
                   BigDecimal estoqueAtual, BigDecimal estoqueMinimo, BigDecimal precoUnitario) {
        this();
        this.nome = nome;
        this.codigo = codigo;
        this.categoria = categoria;
        this.unidadeMedida = unidadeMedida;
        this.estoqueAtual = estoqueAtual;
        this.estoqueMinimo = estoqueMinimo;
        this.precoUnitario = precoUnitario;
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Método para verificar se o estoque está baixo
    public boolean isEstoqueBaixo() {
        return estoqueAtual.compareTo(estoqueMinimo) <= 0;
    }

    // Getters e Setters
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(BigDecimal estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public BigDecimal getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(BigDecimal estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public List<MovimentacaoMaterial> getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(List<MovimentacaoMaterial> movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public List<MaterialConsulta> getMateriaisConsulta() {
        return materiaisConsulta;
    }

    public void setMateriaisConsulta(List<MaterialConsulta> materiaisConsulta) {
        this.materiaisConsulta = materiaisConsulta;
    }
}