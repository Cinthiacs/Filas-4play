package com.example.filas4play.model;

public class Brinquedo {
    private String nome;
    private int imagemResId;

    public Brinquedo(String nome, int imagemResId) {
        this.nome = nome;
        this.imagemResId = imagemResId;
    }

    public String getNome() {
        return nome;
    }

    public int getImagemResId() {
        return imagemResId;
    }

    @Override
    public String toString() {
        return "Brinquedo{" +
                "nome='" + nome + '\'' +
                ", imagemResId=" + imagemResId +
                '}';
    }
}
