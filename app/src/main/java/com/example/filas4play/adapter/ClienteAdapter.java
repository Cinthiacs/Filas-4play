package com.example.filas4play.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filas4play.R;
import com.example.filas4play.model.Cliente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> listaClientes;

    public ClienteAdapter(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_cliente, parent, false); // Usando o layout com o RecyclerView aninhado
        return new ClienteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = listaClientes.get(position);
        holder.txtNome.setText("Nome: " + cliente.getNome());
        holder.txtPublico.setText("Público: " + cliente.getTipoPublico());
        holder.txtContato.setText("Contato: " + cliente.getContato());
        holder.txtNascimento.setText("Nascimento: " + cliente.getDtnasc());
        holder.txtEndereco.setText("Endereço: " + cliente.getLogradouro() + ", " +
                (cliente.getComplemento() != null && !cliente.getComplemento().isEmpty() ? cliente.getComplemento() + ", " : "") +
                cliente.getBairro() + ", " + cliente.getCidade() + " - " + cliente.getUf());
        holder.txtCep.setText("CEP: " + cliente.getCep());
        holder.txtHistoricoTitulo.setText("Histórico:");

        // Inicialize o RecyclerView do histórico dentro do item
        holder.recyclerBrinquedos.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerBrinquedos.setNestedScrollingEnabled(false); // Importante para evitar problemas de scroll

        // Busque o histórico para este cliente
        DatabaseReference refHistorico = FirebaseDatabase.getInstance().getReference("historico");
        refHistorico.orderByChild("userId").equalTo(cliente.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> historicoList = new ArrayList<>();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String brinquedo = snap.child("nome").getValue(String.class);
                            String dataHora = snap.child("dataHora").getValue(String.class);
                            historicoList.add("- " + brinquedo + " em " + dataHora);
                        }

                        holder.recyclerBrinquedos.setAdapter(new HistoricoAdapter(historicoList.isEmpty() ? List.of("Nenhum histórico disponível") : historicoList));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.recyclerBrinquedos.setAdapter(new HistoricoAdapter(List.of("Erro ao carregar histórico")));
                        Log.e("CLIENTE_ADAPTER", "Erro ao carregar histórico: " + error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtNascimento, txtEndereco, txtContato, txtCep, txtPublico, txtHistoricoTitulo;
        RecyclerView recyclerBrinquedos;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeUsuario);
            txtNascimento = itemView.findViewById(R.id.txtNascimento);
            txtEndereco = itemView.findViewById(R.id.txtEndereco);
            txtContato = itemView.findViewById(R.id.txtContato);
            txtCep = itemView.findViewById(R.id.txtCep);
            txtPublico = itemView.findViewById(R.id.textViewPublico);
            txtHistoricoTitulo = itemView.findViewById(R.id.txtHistorico); // Usado como título
            recyclerBrinquedos = itemView.findViewById(R.id.recyclerBrinquedos);
        }
    }
}