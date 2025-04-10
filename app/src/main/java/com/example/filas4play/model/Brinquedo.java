package com.example.filas4play.model;

public class Brinquedo {
    private String nome;
    private int imagemResId;
    private String data;


    public Brinquedo(String nome, int imagemResId) {
        this.nome = nome;
        this.imagemResId = imagemResId;
    }

     public Brinquedo(String nome, int imagemResId, String data) {
        this.nome = nome;
        this.imagemResId = imagemResId;
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public int getImagemResId() {
        return imagemResId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Brinquedo{" +
                "nome='" + nome + '\'' +
                ", imagemResId=" + imagemResId +
                ", data='" + data + '\'' +
                '}';
    }
}
