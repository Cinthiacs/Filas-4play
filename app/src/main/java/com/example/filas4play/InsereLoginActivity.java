/*
package com.example.filas4play;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.filas4play.MainActivity;
import com.example.filas4play.InsereLoginActivity;

public class InsereLoginActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button buttonSalvar;
    private ProgressBar progressBar;
    private Button btnLogout;
    private Button btnRedefinirSenha;
    private FirebaseUser user;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insere_login);

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        buttonSalvar = findViewById(R.id.btn_salvar);
        progressBar = findViewById(R.id.progressBar2);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent i = new Intent(getApplicationContext(), InsereLoginActivity.class);
            startActivity(i);
            finish();
        } else {
            //txtUsuario.setText(user.getEmail());
        }

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
          //  Intent i = new Intent(getApplicationContext(), LoginActivity.class);
          //  startActivity(i);
            finish();
        });

        btnRedefinirSenha.setOnClickListener(v -> {
           // redefinirSenha();
        });

        buttonSalvar.setOnClickListener(v -> inserirUsuario());
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void inserirUsuario() {
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(editEmail.getText().toString(), editSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(InsereLoginActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), InsereLoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(InsereLoginActivity.this, "Falha ao inserir usuário", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}*/
