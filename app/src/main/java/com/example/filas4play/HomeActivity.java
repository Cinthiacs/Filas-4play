package com.example.filas4play;

import android.os.Bundle;
import android.widget.RadioButton;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private RadioGroup rgBbrinquedos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicialização dos elementos da UI
        txtEmailUsuario = findViewById(R.id.txt_email_usuario);
        txtPublico = findViewById(R.id.textViewPublico);
        txtNomeUsuario = findViewById(R.id.textViewUsuario);

        btn_logout = findViewById(R.id.btn_logout);
        btn_redefinir_senha = findViewById(R.id.btn_redefinir_senha);

        viewPager = findViewById(R.id.brinquedosViewPager);
        tabLayout = findViewById(R.id.tabLayout);
        rgBbrinquedos = findViewById(R.id.rgBrinquedos);
        Button btnFila = findViewById(R.id.btn_fila);

        // Configuração do Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cliente").child(userId);
            String email = user.getEmail();

            txtEmailUsuario.setText("Email: " + email);

            // Carrega dados do usuário
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

        // Criação da lista de brinquedos para exibir no ViewPager
        String dataHoje = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        List<Brinquedo> brinquedos = new ArrayList<>();
        brinquedos.add(new Brinquedo("Balão", R.drawable.balao_passeio));
        brinquedos.add(new Brinquedo("Carrossel", R.drawable.carrossel_gigante));
        brinquedos.add(new Brinquedo("Roda Gigante", R.drawable.roda_gigante));
        brinquedos.add(new Brinquedo("Loop", R.drawable.montanha_russa));

        // Configura o adapter do ViewPager
        BrinquedoAdapter adapter = new BrinquedoAdapter(this, brinquedos);
        viewPager.setAdapter(adapter);

        // Configura a TabLayout para acompanhar as mudanças do ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(brinquedos.get(position).getNome());
                }).attach();

        // Ação do botão para adicionar à fila
        btnFila.setOnClickListener(v -> {
            int selectId = rgBbrinquedos.getCheckedRadioButtonId();

            if (selectId != -1) {
                RadioButton radioButton = findViewById(selectId);
                String nomeBrinquedo = radioButton.getText().toString();
                int drawableId = getDrawableIdByBrinquedo(nomeBrinquedo);  // Obtém o ID da imagem correspondente

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference refHistorico = FirebaseDatabase.getInstance().getReference("historico")
                        .child(userId)
                        .child(dataHoje);

                // Verifica se já existe um registro com o brinquedo para o dia
                refHistorico.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Verifica se o brinquedo já foi registrado para o dia
                            boolean brinquedoExistente = false;
                            for (DataSnapshot brinquedoSnapshot : snapshot.getChildren()) {
                                String brinquedoNome = brinquedoSnapshot.child("nome").getValue(String.class);
                                if (brinquedoNome != null && brinquedoNome.equals(nomeBrinquedo)) {
                                    brinquedoExistente = true;
                                    break;
                                }
                            }
                            if (!brinquedoExistente) {
                                // Adiciona o brinquedo se não existir
                                refHistorico.push().setValue(new Brinquedo(nomeBrinquedo, drawableId));
                            } else {
                                Toast.makeText(HomeActivity.this, "Brinquedo já foi adicionado hoje.", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            refHistorico.push().setValue(new Brinquedo(nomeBrinquedo, drawableId));
                        }

                        Intent intent = new Intent(HomeActivity.this, FilaActivity.class);  // Certifique-se de que a Activity Fila está criada
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Erro ao verificar histórico.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(HomeActivity.this, "Por favor, selecione um brinquedo", Toast.LENGTH_SHORT).show();
            }
        });

        // Ação do botão para logout
        btn_logout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        });

        // Ação do botão para redefinir senha
        btn_redefinir_senha.setOnClickListener(v -> redefinirSenha());
    }

    // Função para redefinir senha
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

    // Função para obter o drawableId do brinquedo selecionado
    private int getDrawableIdByBrinquedo(String nomeBrinquedo) {
        switch (nomeBrinquedo) {
            case "Balão":
                return R.drawable.balao_passeio;
            case "Carrossel":
                return R.drawable.carrossel_gigante;
            case "Roda Gigante":
                return R.drawable.roda_gigante;
            case "Loop":
                return R.drawable.montanha_russa;
            default:
                return -1;  // Caso não encontre o nome do brinquedo
        }
    }
}
