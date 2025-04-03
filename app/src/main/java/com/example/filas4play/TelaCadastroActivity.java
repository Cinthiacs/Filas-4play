package com.example.filas4play;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import androidx.activity.EdgeToEdge;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filas4play.api.ViaCepService;
import com.example.filas4play.model.Endereco;
//import com.example.filas4play.model.Cliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class TelaCadastroActivity extends AppCompatActivity {
    private EditText edt_nome_register, edt_datanasc_register, edt_contato_register;
    private EditText edt_cep_register, edt_logradouro_register, edt_complemento_register;
    private EditText edt_bairro_register, edt_cidade_register, edt_uf_register;

    private CheckBox checkGeral, checkInfantil, checkPcd, checkIdoso;

    private Button btnBuscarCep, btnSalvar;

    private ViaCepService viaCepService;
    private Retrofit retrofitViaCep;
    private ImageButton btnVoltar;
    Spinner spinnerPublico;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro);


        edt_nome_register = findViewById(R.id.edt_nome);
        edt_datanasc_register = (EditText) findViewById(R.id.edt_dtnasc);
        edt_contato_register = (EditText) findViewById(R.id.edt_contato);
        edt_cep_register = findViewById(R.id.edt_cep);
        edt_logradouro_register = findViewById(R.id.edt_logradouro);
        edt_complemento_register = findViewById(R.id.edt_complemento);
        edt_bairro_register = findViewById(R.id.edt_bairro);
        edt_cidade_register = findViewById(R.id.edt_cidade);
        edt_uf_register = findViewById(R.id.edt_uf);
        progressBar = findViewById(R.id.progressBar);

        btnBuscarCep = findViewById(R.id.btnBuscarCep);

        btnSalvar = findViewById(R.id.btn_salvar);

        btnVoltar = findViewById(R.id.btn_voltar);

        mAuth = FirebaseAuth.getInstance();

        spinnerPublico = findViewById(R.id.spinner_publico);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.publico_array, // Crie um array de strings em strings.xml
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublico.setAdapter(adapter);


        // Configurando o clique do botão de voltar
        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(TelaCadastroActivity.this, MainActivity.class);
            startActivity(intent);
        });

        retrofitViaCep = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        viaCepService = retrofitViaCep.create(ViaCepService.class);

        btnBuscarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarCep();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarCliente();

            }
        });
    }

    private void buscarCep() {
        String cep = this.edt_cep_register.getText().toString().trim();
        if (!cep.isEmpty() && cep.length() == 8) {
            Call<Endereco> call = viaCepService.getEndereco(cep);
            call.enqueue(new Callback<Endereco>() {
                @Override
                public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Endereco endereco = response.body();
                        edt_logradouro_register.setText(endereco.getLogradouro());
                        edt_complemento_register.setText(endereco.getComplemento());
                        edt_bairro_register.setText(endereco.getBairro());
                        edt_cidade_register.setText(endereco.getLocalidade());
                        edt_uf_register.setText(endereco.getUf());
                    } else {
                        Toast.makeText(TelaCadastroActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Endereco> call, Throwable t) {
                    Toast.makeText(TelaCadastroActivity.this, "Erro ao buscar CEP", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(TelaCadastroActivity.this, "CEP inválido", Toast.LENGTH_SHORT).show();
        }
    }
    private void salvarCliente() {
       /* Cliente cliente = new Cliente();
//        progressBar.setVisibility(View.VISIBLE);

        cliente.setNome(edt_nome_register.getText().toString());
        cliente.setCep(edt_cep_register.getText().toString());
        cliente.setLogradouro(edt_logradouro_register.getText().toString());
        cliente.setComplemento(edt_complemento_register.getText().toString());
        cliente.setBairro(edt_bairro_register.getText().toString());
        cliente.setCidade(edt_cidade_register.getText().toString());
        cliente.setUf(edt_uf_register.getText().toString());
        cliente.setDtnasc(edt_datanasc_register.getText().toString());
        cliente.setContato(edt_contato_register.getText().toString());
        cliente.setTipoPublico(spinnerPublico.getSelectedItem().toString());*/

        databaseReference = FirebaseDatabase.getInstance().getReference("cliente");

        databaseReference.setValue("foi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Num Foi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//           cliente.setId(databaseReference.push().getKey());
//           cliente.salvar();

//          String chave = databaseReference.push().getKey();

        //          databaseReference.child(chave).setValue(cliente, new DatabaseReference.CompletionListener() {
        //              @Override
        //              public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                if (databaseError == null) {
//                    Toast.makeText(TelaCadastroActivity.this, "Cliente salvo com sucesso!", Toast.LENGTH_SHORT).show();
//                    limparCampos();
//
//                    Intent intent = new Intent(TelaCadastroActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//
//                } else {
//                    Toast.makeText(TelaCadastroActivity.this, "Erro ao salvar cliente: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    private void limparCampos() {
        edt_nome_register.setText("");
        edt_datanasc_register.setText("");
        edt_contato_register.setText("");
        edt_cep_register.setText("");
        edt_logradouro_register.setText("");
        edt_complemento_register.setText("");
        edt_bairro_register.setText("");
        edt_cidade_register.setText("");
        edt_uf_register.setText("");

        checkGeral.setChecked(false);
        checkInfantil.setChecked(false);
        checkPcd.setChecked(false);
        checkIdoso.setChecked(false);
    }
}


