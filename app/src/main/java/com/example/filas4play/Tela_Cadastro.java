package com.example.filas4play;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

public class Tela_Cadastro extends AppCompatActivity {
    ImageButton btn_voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);
        btn_voltar = findViewById(R.id.btn_voltar);

        btn_voltar.setOnClickListener(v ->{
            Intent intent = new Intent(Tela_Cadastro.this, MainActivity.class);
            startActivity(intent);
    });
    }
}