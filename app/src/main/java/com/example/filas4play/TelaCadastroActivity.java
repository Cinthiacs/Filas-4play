package com.example.filas4play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.filas4play.api.ViaCepService;
import com.example.filas4play.model.Endereco;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TelaCadastroActivity extends AppCompatActivity {
    private EditText nome, datanasc,     contato,cep, logradouro, complemento, bairro, cidade, uf;
    private CheckBox checkGeral, checkInfantil, checkPcd, checkIdoso;
    private Button btnBuscarCep, btnSalvar;

    private ViaCepService viaCepService;
    private Retrofit retrofitViaCep;
    private ImageButton btnVoltar;

    Spinner spinnerPublico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro);


        nome = findViewById(R.id.nome);
        datanasc = (EditText) findViewById(R.id.dtnasc);
        contato = (EditText) findViewById(R.id.contato);
        cep = findViewById(R.id.cep);
        logradouro = findViewById(R.id.logradouro);
        complemento = findViewById(R.id.complemento);
        bairro = findViewById(R.id.bairro);
        cidade = findViewById(R.id.cidade);
        uf = findViewById(R.id.uf);

        btnBuscarCep = findViewById(R.id.btnBuscarCep);

        spinnerPublico = findViewById(R.id.spinner_publico);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.publico_opcoes,
                android.R.layout.simple_spinner_item
        );
        List<String> list = new ArrayList<>();
        list.add("Público");
        for (int i = 0; i < adapter.getCount(); i++) {
            list.add(adapter.getItem(i).toString());
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublico.setAdapter(adapter);


        btnSalvar = findViewById(R.id.btn_salvar);
        btnVoltar = findViewById(R.id.btn_voltar);

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

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CadastroClienteActivity", "onDestroy called");
    }

    private void buscarCep() {
        String cep = this.cep.getText().toString().trim();
        if (!cep.isEmpty() && cep.length() == 8) {
            Call<Endereco> call = viaCepService.getEndereco(cep);
            call.enqueue(new Callback<Endereco>() {
                @Override
                public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Endereco endereco = response.body();
                        logradouro.setText(endereco.getLogradouro());
                        complemento.setText(endereco.getComplemento());
                        bairro.setText(endereco.getBairro());
                        cidade.setText(endereco.getLocalidade());
                        uf.setText(endereco.getUf());
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

}

