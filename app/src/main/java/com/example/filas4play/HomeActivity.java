package com.example.filas4play;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.filas4play.adapter.BrinquedoAdapter;
import com.example.filas4play.model.Brinquedo;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private TextView txtEmailUsuario;
    private TextView txtPublico;
    private TextView txtNomeUsuario;

    private Button btn_logout;
    private FirebaseAuth mAuth;

    private Button btn_redefinir_senha;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    private List<Integer> imagensBrinquedos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtEmailUsuario = findViewById(R.id.txt_email_usuario);
        txtPublico = findViewById(R.id.textViewPublico);
        txtNomeUsuario = findViewById(R.id.textViewUsuario);

        btn_logout = findViewById(R.id.btn_logout);
        btn_redefinir_senha = findViewById(R.id.btn_redefinir_senha);

        viewPager = findViewById(R.id.brinquedosViewPager);
        tabLayout = findViewById(R.id.tabLayout);

        List<Brinquedo> brinquedos = new ArrayList<>();
        brinquedos.add(new Brinquedo("Balão", R.drawable.balao_passeio));
        brinquedos.add(new Brinquedo("Carrossel", R.drawable.carrossel_gigante));
        brinquedos.add(new Brinquedo("Roda Gigante", R.drawable.roda_gigante));
        brinquedos.add(new Brinquedo("Loop", R.drawable.montanha_russa));

        BrinquedoAdapter adapter = new BrinquedoAdapter(this, brinquedos);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(brinquedos.get(position).getNome());
                }).attach();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cliente").child(userId);
            String email = user.getEmail();

            txtEmailUsuario.setText("Email: " + email);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nome = snapshot.child("nome").getValue(String.class);
                        String publico = snapshot.child("tipoPublico").getValue(String.class);
                        txtNomeUsuario.setText("🎉 Bem-vindo(a): " + nome);
                        txtPublico.setText("Publico: " + publico);
                    } else {
                        txtNomeUsuario.setText("Nome não encontrado");
                        txtPublico.setText("Publico não encontrado");
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
