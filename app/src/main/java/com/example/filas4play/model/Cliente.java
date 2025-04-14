package com.example.filas4play.model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Cliente {
    private String id;
    private String dtnasc;
    private String nome;
    private String contato;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;

    private String email;

    private String senha;
    private String confirmasenha;
    private String tipoPublico;

    private List<String> historico;

    // Construtor padrão
    public Cliente() {

    }

    public Cliente(String nome, String dtnasc, String contato, String cep, String logradouro, String complemento,
                   String bairro, String cidade, String uf, String email, String senha, String confirmasenha, String tipoPublico) {
        this.id = null;
        this.nome = nome;
        this.dtnasc = dtnasc;
        this.contato = contato;
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.email = email;
        this.senha = senha;
        this.tipoPublico = tipoPublico;
        this.confirmasenha = confirmasenha;
    }


    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) { // Corrigido para "setId"
        this.id = id;
    }

    public String getDtnasc() {
        return dtnasc;
    }

    public void setDtnasc(String dtnasc) {
        this.dtnasc = dtnasc;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
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
    public String getConfirmaSenha() {
        return confirmasenha;
    }

    public void setConfirmaSenha(String ConfirmaSenha) {
        this.confirmasenha = ConfirmaSenha;
    }

    public String getTipoPublico() {
        return tipoPublico;
    }

    public void setTipoPublico(String TipoPublico) {
        this.tipoPublico = TipoPublico;
    }

    public List<String> getHistorico() {
        return historico;
    }

    public void setHistorico(List<String> historico) {
        this.historico = historico;
    }




    public void salvar() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("cliente").child(this.id).setValue(this);
    }

    public static class Historico {
        private String nome;
        private String dataHora;


        public Historico() {}

        public Historico(String nome, String dataHora) {
            this.nome = nome;
            this.dataHora = dataHora;
        }

        // Getters e Setters
        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDataHora() {
            return dataHora;
        }

        public void setDataHora(String dataHora) {
            this.dataHora = dataHora;
        }
    }
}




