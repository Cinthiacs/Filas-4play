package com.example.filas4play.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.filas4play.R;
import  com.example.filas4play.model.Cliente;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {


    private List<Cliente> clientes;

    public ClienteAdapter(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_cliente, parent, false);
        return new ClienteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.tvDtNasc.setText("Data Nascimento: " + cliente.getDtnasc());
        holder.tvNome.setText("Nome: " + cliente.getNome());
        holder.tvEndereco.setText("Endereço: " + cliente.getLogradouro() + ", " + cliente.getComplemento() + " - " + cliente.getBairro() + ", " + cliente.getCidade() + " - " + cliente.getUf());
        holder.tvCep.setText("CEP: " + cliente.getCep());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvDtNasc, tvNome, tvEndereco, tvCep;

        ClienteViewHolder(View itemView) {
            super(itemView);
            tvDtNasc = itemView.findViewById(R.id.item_dtnasc);
            tvNome = itemView.findViewById(R.id.item_nome);
            tvEndereco = itemView.findViewById(R.id.item_endereco);
            tvCep = itemView.findViewById(R.id.item_cep);
        }
    }
}
