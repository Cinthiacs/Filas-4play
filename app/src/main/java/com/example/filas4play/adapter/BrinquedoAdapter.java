package com.example.filas4play.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filas4play.R;
import com.example.filas4play.model.Brinquedo;

import java.util.List;

public class BrinquedoAdapter extends RecyclerView.Adapter<BrinquedoAdapter.BrinquedoViewHolder> {

    private Context context;
    private List<Brinquedo> brinquedos;

    public BrinquedoAdapter(Context context, List<Brinquedo> brinquedos) {
        this.context = context;
        this.brinquedos = brinquedos;
    }

    @NonNull
    @Override
    public BrinquedoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brinquedo, parent, false);
        return new BrinquedoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrinquedoViewHolder holder, int position) {
        Brinquedo brinquedo = brinquedos.get(position);
        holder.imageView.setImageResource(brinquedo.getImagemResId());
    }

    @Override
    public int getItemCount() {
        return brinquedos.size();
    }

    public class BrinquedoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public BrinquedoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_brinquedo);
        }
    }
}