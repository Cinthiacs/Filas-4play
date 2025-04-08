package com.example.filas4play;

import android.os.Bundle;
import android.content.Intent;
import android.text.InputType;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView btn_cadastro;
    private EditText edt_email_register, edt_senha_register;
    private Button btn_login;

    private Button btn_funcionarios;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa os elementos da interface
        btn_cadastro = findViewById(R.id.btn_cadastro_click);
        btn_login = findViewById(R.id.btn_abre_tela_Login);
        btn_funcionarios = findViewById(R.id.btn_funcionario);
        edt_email_register = findViewById(R.id.edt_email);
        edt_senha_register = findViewById(R.id.edt_senha);
        progressBar = findViewById(R.id.progressBar);


        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ImageView imgToggleSenha = findViewById(R.id.img_toggle_senha);

        final boolean[] isSenhaVisivel = {false};

        imgToggleSenha.setOnClickListener(v -> {
            if (isSenhaVisivel[0]) {
                // Ocultar senha
                edt_senha_register.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                imgToggleSenha.setImageResource(R.drawable.ic_eye); // Ícone de olho fechado
            } else {
                // Mostrar senha
                edt_senha_register.setInputType(InputType.TYPE_CLASS_NUMBER);
                imgToggleSenha.setImageResource(R.drawable.ic_eye); // Você pode mudar para ic_eye_open se tiver um
            }
            edt_senha_register.setSelection(edt_senha_register.length()); // Mantém o cursor no final
            isSenhaVisivel[0] = !isSenhaVisivel[0];
        });

        // Ações dos botões
        btn_cadastro.setOnClickListener(v -> cadastrar());
        btn_login.setOnClickListener(v -> entrar());
        btn_funcionarios.setOnClickListener(v -> funcionarios());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Ir para próxima tela se já estiver logado
            Intent i = new Intent(getApplicationContext(), HomeActivity.class); // Altere para sua tela principal
            startActivity(i);
            finish();
        }
    }

    private void entrar() {

        String email = edt_email_register.getText().toString();
        String senha = edt_senha_register.getText().toString();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(MainActivity .this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class); // Ir para a tela principal
                        startActivity(i);
                        finish();
                    } else {
                        // Falha no login
                        Toast.makeText(MainActivity.this, "Falha na autenticação.", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }

    private void cadastrar() {
        Intent i = new Intent(MainActivity.this, TelaCadastroActivity.class);
        startActivity(i);
    }
    private void funcionarios(){
        Intent i = new Intent(MainActivity.this,ItemClienteActivity.class);
        startActivity(i);
    }

}
