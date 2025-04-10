package com.example.filas4play;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Color;

public class FilaActivity extends AppCompatActivity {

    private TextView txtPublico;
    private TextView txtNomeUsuario;
    private TextView txtPosicao;

    private Button btn_sair_fila;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fila);
        txtPublico = findViewById(R.id.textViewPublico);
        txtNomeUsuario = findViewById(R.id.textViewUsuario);
        btn_sair_fila = findViewById(R.id.btn_sair_fila);
        TextView txtPosicao = findViewById(R.id.textViewPosicao);
        final Handler handler = new Handler(Looper.getMainLooper());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        String userId = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cliente").child(userId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nome = snapshot.child("nome").getValue(String.class);
                    String publico = snapshot.child("tipoPublico").getValue(String.class);

                    txtNomeUsuario.setText(nome);
                    txtPublico.setText(publico);

                } else {
                    txtNomeUsuario.setText("Nome não encontrado");
                    txtPublico.setText("Publico não encontrado");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FilaActivity.this, "Erro ao carregar nome", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(FilaActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        int[] posicao = {10};

        Runnable filaRunnable = new Runnable() {
            @Override
            public void run() {
                txtPosicao.setText("Posição na fila: " + posicao[0]);

                if (posicao[0] == 3) {
                    txtPosicao.setTextColor(Color.parseColor("#D32F2F"));
                    txtPosicao.setBackgroundColor(Color.parseColor("#FFF9C4"));
                    txtPosicao.setText("⚠️ Sua vez está próxima!");
                }

                if (posicao[0] == 1) {
                    txtPosicao.setTextColor(Color.WHITE);
                    txtPosicao.setBackgroundColor(Color.RED);
                    txtPosicao.setText("🎉 É a sua vez!");
                }

                if (posicao[0] > 1) {
                    posicao[0]--;
                    handler.postDelayed(this, 3000);
                }
            }
        };

        handler.post(filaRunnable);
        btn_sair_fila.setOnClickListener(v->{
            Intent i = new Intent(FilaActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        });

    }

}

