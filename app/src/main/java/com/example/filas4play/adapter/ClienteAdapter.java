package com.example.filas4play.adapter;

import com.example.filas4play.model.Cliente;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.filas4play.R;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> listaClientes;

    public ClienteAdapter(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
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
        Cliente cliente = listaClientes.get(position);
        holder.txtNome.setText("Nome: " + cliente.getNome());
        holder.txtPublico.setText("Público: " + cliente.getTipoPublico());
        holder.txtContato.setText("Contato: " + cliente.getContato());
        holder.txtNascimento.setText("Nascimento: " + cliente.getDtnasc());
        holder.txtEndereco.setText("Endereço: " + cliente.getLogradouro() + ", " +
                (cliente.getComplemento().isEmpty() ? "" : cliente.getBairro() + ", ") +
                cliente.getCidade() + ", " + cliente.getUf() + " - ");
        holder.txtCep.setText("CEP: " + cliente.getCep());
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtNascimento, txtEndereco, txtContato, txtCep, txtPublico;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeUsuario);
            txtNascimento = itemView.findViewById(R.id.txtNascimento);
            txtEndereco = itemView.findViewById(R.id.txtEndereco);
            txtContato = itemView.findViewById(R.id.txtContato);
            txtCep = itemView.findViewById(R.id.txtCep);
            txtPublico = itemView.findViewById(R.id.textViewPublico);
        }
    }
}
