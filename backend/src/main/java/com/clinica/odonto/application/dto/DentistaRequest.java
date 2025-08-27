package com.clinica.odonto.application.dto;

import com.clinica.odonto.domain.entity.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class DentistaRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

    @NotBlank(message = "CRO é obrigatório")
    @Size(max = 20, message = "CRO deve ter no máximo 20 caracteres")
    private String cro;

    @Size(max = 100, message = "Especialidade deve ter no máximo 100 caracteres")
    private String especialidade;

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    private String telefone;
    
    private List<Long> planosAceitosIds;

    public DentistaRequest() {}

    public DentistaRequest(String nome, String email, String senha, String cro, String especialidade, String telefone) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cro = cro;
        this.especialidade = especialidade;
        this.telefone = telefone;
    }

    // Getters and Setters
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public List<Long> getPlanosAceitosIds() {
        return planosAceitosIds;
    }
    
    public void setPlanosAceitosIds(List<Long> planosAceitosIds) {
        this.planosAceitosIds = planosAceitosIds;
    }
}