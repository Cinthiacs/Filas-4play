package com.example.filas4play;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filas4play.adapter.ClienteAdapter;
import com.example.filas4play.model.Cliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaClientesActivity extends AppCompatActivity {

    private RecyclerView recyclerClientes;
    private ClienteAdapter clienteAdapter;
    private List<Cliente> listaClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);

        recyclerClientes = findViewById(R.id.recyclerClientes);
        recyclerClientes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        listaClientes = new ArrayList<>();
        clienteAdapter = new ClienteAdapter(listaClientes);
        recyclerClientes.setLayoutManager(new LinearLayoutManager(this));

        // Assegure-se de configurar o adaptador primeiro
        recyclerClientes.setAdapter(clienteAdapter);

        carregarClientes();  // Agora a carga dos dados pode ser realizada após a configuração
    }

    private void carregarClientes() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cliente");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaClientes.clear();
                for (DataSnapshot clienteSnapshot : snapshot.getChildren()) {
                    Cliente cliente = clienteSnapshot.getValue(Cliente.class);
                    if (cliente != null) {

                        listaClientes.add(cliente);
                    }
                }
                clienteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListaClientesActivity.this, "Erro ao carregar clientes.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
