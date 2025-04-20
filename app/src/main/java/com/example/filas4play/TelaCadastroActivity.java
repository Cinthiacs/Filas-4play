package com.example.filas4play;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.activity.EdgeToEdge;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;

import com.example.filas4play.api.ViaCepService;
import com.example.filas4play.model.Endereco;
import com.example.filas4play.model.Cliente;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.ProgressBar;
import android.util.Log;

public class TelaCadastroActivity extends AppCompatActivity {
    private EditText edt_nome_register, edt_datanasc_register, edt_contato_register;
    private EditText edt_cep_register, edt_logradouro_register, edt_complemento_register;
    private EditText edt_bairro_register, edt_cidade_register, edt_uf_register;
    private EditText edt_email_register, edt_senha_register, edt_confirmasenha_register;

    private Button btnBuscarCep, btnSalvar;

    private ViaCepService viaCepService;
    private Retrofit retrofitViaCep;

    Spinner spinnerPublico;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

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
        edt_email_register = findViewById(R.id.edt_email);
        edt_senha_register = findViewById(R.id.edt_senha);
        edt_confirmasenha_register = findViewById(R.id.edt_confirmasenha);
        btnBuscarCep = findViewById(R.id.btnBuscarCep);
        btnSalvar = findViewById(R.id.btn_salvar);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        EditText edtSenha = findViewById(R.id.edt_senha);
        ImageView imgToggleSenha = findViewById(R.id.img_toggle_senha);

        EditText edtConfirmaSenha = findViewById(R.id.edt_confirmasenha);
        ImageView imgToggleConfirmar = findViewById(R.id.img_toggle_confirmasenha);

        spinnerPublico = findViewById(R.id.spinner_publico);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.publico_array, // Crie um array de strings em strings.xml
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublico.setAdapter(adapter);

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

        configuraToggleSenha(edtSenha, imgToggleSenha);
        configuraToggleSenha(edtConfirmaSenha, imgToggleConfirmar);
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

    private void configuraToggleSenha(EditText editText, ImageView toggleIcon) {
        final boolean[] isVisible = {false};

        toggleIcon.setOnClickListener(v -> {
            if (isVisible[0]) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                toggleIcon.setImageResource(R.drawable.ic_eye);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                toggleIcon.setImageResource(R.drawable.ic_eye);
            }
            editText.setSelection(editText.length());
            isVisible[0] = !isVisible[0];
        });
    }

    private void salvarCliente() {
        Cliente cliente = new Cliente();

        cliente.setNome(edt_nome_register.getText().toString());
        cliente.setCep(edt_cep_register.getText().toString());
        cliente.setLogradouro(edt_logradouro_register.getText().toString());
        cliente.setComplemento(edt_complemento_register.getText().toString());
        cliente.setBairro(edt_bairro_register.getText().toString());
        cliente.setCidade(edt_cidade_register.getText().toString());
        cliente.setUf(edt_uf_register.getText().toString());
        cliente.setDtnasc(edt_datanasc_register.getText().toString());
        cliente.setContato(edt_contato_register.getText().toString());
        cliente.setEmail(edt_email_register.getText().toString());
        String senha = edt_senha_register.getText().toString();
        String confirmarsenha = edt_confirmasenha_register.getText().toString();
        cliente.setTipoPublico(spinnerPublico.getSelectedItem().toString());


        if (!TextUtils.isEmpty(cliente.getNome()) && !TextUtils.isEmpty(cliente.getDtnasc()) &&
                !TextUtils.isEmpty(cliente.getContato()) && !TextUtils.isEmpty(cliente.getContato()) &&
                !TextUtils.isEmpty(cliente.getCep()) && !TextUtils.isEmpty(cliente.getLogradouro()) &&
                !TextUtils.isEmpty(cliente.getComplemento()) && !TextUtils.isEmpty(cliente.getBairro()) &&
                !TextUtils.isEmpty(cliente.getCidade()) && !TextUtils.isEmpty(cliente.getUf()) &&
                !TextUtils.isEmpty(cliente.getEmail()) && !TextUtils.isEmpty(senha) &&
                !TextUtils.isEmpty(confirmarsenha)) {

            if (!senha.equals(confirmarsenha)) {
                Toast.makeText(TelaCadastroActivity.this, "A senha deve ser a mesma para ambos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (senha.length() < 6) {
                Toast.makeText(TelaCadastroActivity.this, "A senha deve ter no mínimo 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(cliente.getEmail(), senha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                cliente.setId(mAuth.getUid());
                                cliente.salvar();
                                Log.d("FirebaseDebug", "UID: " + mAuth.getUid());

                                Toast.makeText(getApplicationContext(), "Cadastro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                limparCampos();

                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(TelaCadastroActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

        } else {
            Toast.makeText(TelaCadastroActivity.this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
        }

    }
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
        edt_email_register.setText("");
        edt_senha_register.setText("");
        edt_confirmasenha_register.setText("");
        spinnerPublico.setSelection(0);
    }

}