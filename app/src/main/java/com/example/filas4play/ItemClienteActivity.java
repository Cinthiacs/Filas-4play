    package com.example.filas4play;

    import android.content.Intent;
    import android.os.Bundle;

    import android.util.Log;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;


    public class ItemClienteActivity extends AppCompatActivity {

        private TextView txtDtnasc, txtEndereco, txtCep, txtContato;
        private TextView txtPublico;
        private TextView txtNomeUsuario;
        private TextView txtHistorico;
        private FirebaseAuth mAuth;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_item_cliente);

            // Pegando os elementos do XML
            txtPublico = findViewById(R.id.textViewPublico);
            txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
            txtContato = findViewById(R.id.txtContato);
            txtDtnasc = findViewById(R.id.txtNascimento);
            txtEndereco = findViewById(R.id.txtEndereco);
            txtCep = findViewById(R.id.txtCep);
            txtHistorico = findViewById(R.id.txtHistorico);
            mAuth = FirebaseAuth.getInstance();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cliente").child(userId);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String nome = snapshot.child("nome").getValue(String.class);
                            String publico = snapshot.child("tipoPublico").getValue(String.class);
                            String contato = snapshot.child("contato").getValue(String.class);
                            String logradouro = snapshot.child("logradouro").getValue(String.class);
                            String complemento = snapshot.child("complemento").getValue(String.class);
                            String bairro = snapshot.child("bairro").getValue(String.class);
                            String cidade = snapshot.child("cidade").getValue(String.class);
                            String uf = snapshot.child("uf").getValue(String.class);
                            String dtnasc = snapshot.child("dtnasc").getValue(String.class);

                            String enderecoCompleto = logradouro + ", " +
                                    (complemento.isEmpty() ? "" : complemento + ", ") +
                                    bairro + ", " +
                                    cidade + " - " + uf + ", ";

                            String cep = snapshot.child("cep").getValue(String.class);

                            txtNomeUsuario.setText("Nome: " + nome);
                            txtPublico.setText("Publico: " + publico);
                            txtContato.setText("Contato " + contato);
                            txtDtnasc.setText("Data Nascimento " + dtnasc);
                            txtEndereco.setText("Endereço: " + enderecoCompleto);

                            txtCep.setText("CEP " + cep);

                        } else {
                            txtNomeUsuario.setText("Nome não encontrado");
                            txtPublico.setText("Publico não encontrado");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ItemClienteActivity.this, "Erro ao carregar nome", Toast.LENGTH_SHORT).show();
                    }
                });

                DatabaseReference refHistorico = FirebaseDatabase.getInstance().getReference("historico");

                refHistorico.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        StringBuilder historicoString = new StringBuilder();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String userHistoricoId = snap.child("userId").getValue(String.class);

                            Log.d("HISTORICO_DEBUG", "Comparando: Firebase = " + userHistoricoId + " | Logado = " + userId);

                            if (userId.equals(userHistoricoId)) {
                                String brinquedo = snap.child("nome").getValue(String.class);
                                String data = snap.child("dataHora").getValue(String.class);

                                historicoString.append("-Brinquedo: ").append(brinquedo).append(" Data: ").append(data).append("\n");

                                Log.d("HISTORICO_DEBUG", "Adicionado: " + brinquedo + " em " + data);
                            }
                        }

                        if (historicoString.length() == 0) {
                            historicoString.append("Nenhum histórico encontrado.");
                            Log.d("HISTORICO_DEBUG", "Nenhum histórico encontrado para este usuário.");
                        }

                        txtHistorico.setText(historicoString.toString());
                        Log.d("HISTORICO_DEBUG", "Resultado final:\n" + historicoString.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ItemClienteActivity.this, "Erro ao carregar histórico", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(ItemClienteActivity.this, "Usuário não logado. Redirecionando...", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ItemClienteActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        }
    }