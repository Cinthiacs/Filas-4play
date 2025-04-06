package com.example.filas4play;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    TextView btn_cadastro;

    private Button btn_login;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cadastro = findViewById(R.id.btn_cadastro_click);
        btn_cadastro.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, TelaCadastroActivity.class);
            startActivity(intent);
        });

//        btn_login = findViewById(R.id.btn_abre_tela_Login);
//        btn_login.setOnClickListener(v ->{
//            Intent intent = new Intent( MainActivity.this,InsereLoginActivity.class);
//            startActivity(intent);
//        });

    }
}