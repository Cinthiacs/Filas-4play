package com.example.filas4play.model;

public class Brinquedo {
    private String nome;
    private int imagemResId;
    private String userId;
    private String dataHora;

    public Brinquedo() {}

    public Brinquedo(String nome, int imagemResId) {
        this.nome = nome;
        this.imagemResId = imagemResId;
    }

    public Brinquedo(String nome, int imagemResId, String userId, String dataHora) {
        this.nome = nome;
        this.imagemResId = imagemResId;
        this.userId = userId;
        this.dataHora = dataHora;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public int getImagemResId() { return imagemResId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }

    @Override
    public String toString() {
        return "Brinquedo{" +
                "nome='" + nome + '\'' +
                ", imagemResId=" + imagemResId +
                ", userId='" + userId + '\'' +
                ", dataHora='" + dataHora + '\'' +
                '}';
    }
}