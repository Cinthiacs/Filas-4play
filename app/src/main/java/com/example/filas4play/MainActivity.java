package com.example.filas4play;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView btn_cadastro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cadastro = findViewById(R.id.btn_cadastro_click);

        btn_cadastro.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, TelaCadastroActivity.class);
            startActivity(intent);
        });

    }
}