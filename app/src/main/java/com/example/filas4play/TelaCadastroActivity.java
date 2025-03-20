package com.example.filas4play;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageButton;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.filas4play.R;
import com.example.filas4play.api.ClienteService;
import com.example.filas4play.api.ViaCepService;
import com.example.filas4play.model.Cliente;
import com.example.filas4play.model.Endereco;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class TelaCadastroActivity extends AppCompatActivity {
    private EditText nome,datanasc, cep, logradouro, complemento, bairro, cidade, uf, geral, infantil, pcd, idoso;
    private Button btnBuscarCep, btnSalvar;

    private ViaCepService viaCepService;
    private ClienteService clienteService;
    private Retrofit retrofitViaCep, retrofitMockApi;

    private ImageButton btnVoltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_cadastro);

        datanasc = (EditText) findViewById(R.id.dtnasc);
        nome = findViewById(R.id.nome);
        cep = findViewById(R.id.cep);
        logradouro = findViewById(R.id.logradouro);
        complemento = findViewById(R.id.complemento);
        bairro = findViewById(R.id.bairro);
        cidade = findViewById(R.id.cidade);
        uf = findViewById(R.id.uf);

        btnBuscarCep = findViewById(R.id.btnBuscarCep);
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

        retrofitMockApi = new Retrofit.Builder()
                //.baseUrl("https://66567a9d9f970b3b36c585db.mockapi.io/")
                .baseUrl("https://6739ffaea3a36b5a62f064bb.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        viaCepService = retrofitViaCep.create(ViaCepService.class);
        clienteService = retrofitMockApi.create(ClienteService.class);




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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CadastroAlunoActivity", "onDestroy called");
    }

    private void buscarCep() {
        String cep = this.cep.getText().toString().trim();  // Corrigir aqui, usando a variável correta
        if (!cep.isEmpty() && cep.length() == 8) {  // Verifique se o CEP tem 8 caracteres
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

    private void salvarCliente() {
        Log.d("salvarCliente", "Iniciando método salvarCliente");

        // Pegando os valores corretos dos campos
        String raText = datanasc.getText().toString().trim();
        String nomeText = nome.getText().toString().trim();
        String cepText = cep.getText().toString().trim();
        String logradouroText = logradouro.getText().toString().trim();
        String complementoText = complemento.getText().toString().trim();
        String bairroText = bairro.getText().toString().trim();
        String cidadeText = cidade.getText().toString().trim();
        String ufText = uf.getText().toString().trim();

        // Verificar se todos os campos obrigatórios estão preenchidos
        if (raText.isEmpty() || nomeText.isEmpty() || cepText.isEmpty() || logradouroText.isEmpty() ||
                bairroText.isEmpty() || cidadeText.isEmpty() || ufText.isEmpty()) {
            Toast.makeText(TelaCadastroActivity.this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertendo a data nasc para inteiro
        int raInt = Integer.parseInt(raText);

        // Criando o objeto cliente
        Cliente cliente = new Cliente(raInt, nomeText, cepText, logradouroText, complementoText, bairroText, cidadeText, ufText);
        Log.d("salvarCliente", "Cliente criado: " + cliente);

        // Chamando a API para salvar o cliente
        Call<Cliente> call = clienteService.salvarCliente(cliente);
        call.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if (response.isSuccessful()) {
                    Log.d("salvarCliente", "Cliente salvo com sucesso");
                    Toast.makeText(TelaCadastroActivity.this, "Cliente salvo com sucesso", Toast.LENGTH_SHORT).show();
                    // Limpar os campos após salvar
                    datanasc.setText("");
                    nome.setText("");
                    cep.setText("");
                    logradouro.setText("");
                    complemento.setText("");
                    bairro.setText("");
                    cidade.setText("");
                    uf.setText("");
                } else {
                    Log.e("salvarCliente", "Erro ao salvar cliente: " + response.message());
                    Toast.makeText(TelaCadastroActivity.this, "Erro ao salvar cliente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Log.e("salvarCliente", "Erro ao salvar cliente: ", t);
                Toast.makeText(TelaCadastroActivity.this, "Erro ao salvar cliente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}