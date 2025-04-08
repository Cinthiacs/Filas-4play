package com.example.filas4play;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemClienteActivity extends AppCompatActivity {

    private TextView itemNome, itemDtnasc, itemEndereco, itemCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cliente);

        // Pegando os elementos do XML
        itemNome = findViewById(R.id.item_nome);
        itemDtnasc = findViewById(R.id.item_dtnasc);
        itemEndereco = findViewById(R.id.item_endereco);
        itemCep = findViewById(R.id.item_cep);

        // Pegando o usuário logado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cliente").child(userId);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nome = snapshot.child("nome").getValue(String.class);
                        String dtnasc = snapshot.child("dtnasc").getValue(String.class);
                        String logradouro = snapshot.child("logradouro").getValue(String.class);
                        String complemento = snapshot.child("complemento").getValue(String.class);
                        String bairro = snapshot.child("bairro").getValue(String.class);
                        String cidade = snapshot.child("cidade").getValue(String.class);
                        String uf = snapshot.child("uf").getValue(String.class);
                        String cep = snapshot.child("cep").getValue(String.class);

                        itemNome.setText("Nome: " + nome);
                        itemDtnasc.setText("Data de nascimento: " + dtnasc);
                        String enderecoCompleto = logradouro + ", " +
                                (complemento.isEmpty() ? "" : complemento + ", ") +
                                bairro + ", " +
                                cidade + " - " + uf + ", " +
                                "CEP: " + cep;

                        itemEndereco.setText("Endereço: " + enderecoCompleto);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ItemClienteActivity.this, "Erro ao carregar os dados", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}