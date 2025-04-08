package com.example.filas4play;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

    public class HomeActivity extends AppCompatActivity {

    private TextView txtEmailUsuario;

    private TextView txtNomeUsuario;
    private Button btn_logout;
    private FirebaseAuth mAuth;

    private Button btn_redefinir_senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtEmailUsuario = findViewById(R.id.txt_email_usuario);
        txtNomeUsuario = findViewById(R.id.textViewUsuario);
        btn_logout = findViewById(R.id.btn_logout);
        btn_redefinir_senha = findViewById(R.id.btn_redefinir_senha);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cliente").child(userId);
            String email = user.getEmail();

            txtEmailUsuario.setText("Email: " + email);

            // Aqui é onde você busca o nome
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nome = snapshot.child("nome").getValue(String.class);
                        txtNomeUsuario.setText("🎉 Bem-vindo(a): " + nome);
                    } else {
                        txtNomeUsuario.setText("Nome não encontrado");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(HomeActivity.this, "Erro ao carregar nome", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            txtEmailUsuario.setText("Usuário não logado");
            txtNomeUsuario.setText(""); // Limpa nome se não estiver logado
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
