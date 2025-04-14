package com.example.filas4play.adapter;

import com.example.filas4play.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

    public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {

        private List<String> historico;

        public HistoricoAdapter(List<String> historico) {
            this.historico = historico;
        }

        @NonNull
        @Override
        public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_historico, parent, false);
            return new HistoricoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
            String itemHistoricoCompleto = historico.get(position);
            String[] partes = itemHistoricoCompleto.split(" - "); // Adapte o separador se necessário

            if (partes.length == 2) {
                holder.txtItemHistorico.setText(partes[0].trim());
                holder.txtDataHistorico.setText(partes[1].trim());
            } else {
                holder.txtItemHistorico.setText(itemHistoricoCompleto.trim());
                holder.txtDataHistorico.setText("");
            }
        }

        @Override
        public int getItemCount() {
            return historico == null ? 0 : historico.size();
        }

        static class HistoricoViewHolder extends RecyclerView.ViewHolder {
            TextView txtItemHistorico;
            TextView txtDataHistorico;

            public HistoricoViewHolder(@NonNull View itemView) {
                super(itemView);
                txtItemHistorico = itemView.findViewById(R.id.txtItemHistorico);
                txtDataHistorico = itemView.findViewById(R.id.txtDataHistorico);
            }
        }
    }

