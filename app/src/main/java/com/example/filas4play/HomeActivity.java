package com.example.filas4play;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.Toast;

    public class HomeActivity extends AppCompatActivity {

    private TextView txtEmailUsuario;
    private Button btn_logout;
    private FirebaseAuth mAuth;

    private Button btn_redefinir_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtEmailUsuario = findViewById(R.id.txt_email_usuario);
        btn_logout = findViewById(R.id.btn_logout);
        btn_redefinir_senha = findViewById(R.id.btn_redefinir_senha);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            txtEmailUsuario.setText("🎉 Bem-vindo(a): " + email);
        } else {
            txtEmailUsuario.setText("Usuário não logado");
        }

        btn_logout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });

        btn_redefinir_senha.setOnClickListener(v -> {
            redefinirSenha();
        });
    }

    private void redefinirSenha() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String emailAddress = user.getEmail();
        if (emailAddress != null) {
            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(HomeActivity.this, "Email de redefinição enviado!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Erro ao enviar email de redefinição.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(HomeActivity.this, "Não foi possível obter o email do usuário.", Toast.LENGTH_SHORT).show();
        }
    }
}
