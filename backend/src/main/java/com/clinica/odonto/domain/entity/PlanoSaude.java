package com.clinica.odonto.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planos_saude")
public class PlanoSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do plano é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 20, message = "Código ANS deve ter no máximo 20 caracteres")
    @Column(name = "codigo_ans", length = 20)
    private String codigoAns;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    // Relacionamento com pacientes
    @OneToMany(mappedBy = "planoSaude", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Paciente> pacientes = new ArrayList<>();

    // Relacionamento many-to-many com dentistas
    @ManyToMany(mappedBy = "planosAceitos", fetch = FetchType.LAZY)
    private List<Dentista> dentistas = new ArrayList<>();

    // Construtores
    public PlanoSaude() {
    }

    public PlanoSaude(String nome, String codigoAns, String descricao) {
        this.nome = nome;
        this.codigoAns = codigoAns;
        this.descricao = descricao;
        this.ativo = true;
    }

    // Métodos de ciclo de vida
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
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

    public String getCodigoAns() {
        return codigoAns;
    }

    public void setCodigoAns(String codigoAns) {
        this.codigoAns = codigoAns;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public List<Dentista> getDentistas() {
        return dentistas;
    }

    public void setDentistas(List<Dentista> dentistas) {
        this.dentistas = dentistas;
    }

    // Métodos utilitários
    public void addPaciente(Paciente paciente) {
        pacientes.add(paciente);
        paciente.setPlanoSaude(this);
    }

    public void removePaciente(Paciente paciente) {
        pacientes.remove(paciente);
        paciente.setPlanoSaude(null);
    }

    public void addDentista(Dentista dentista) {
        dentistas.add(dentista);
        dentista.getPlanosAceitos().add(this);
    }

    public void removeDentista(Dentista dentista) {
        dentistas.remove(dentista);
        dentista.getPlanosAceitos().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanoSaude)) return false;
        PlanoSaude that = (PlanoSaude) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PlanoSaude{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codigoAns='" + codigoAns + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}