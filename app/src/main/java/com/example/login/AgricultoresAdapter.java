package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.ImageView;

public class AgricultoresAdapter extends RecyclerView.Adapter<AgricultoresAdapter.AgricultorViewHolder> {

    private final List<Agricultor> listaAgricultores;

    public AgricultoresAdapter(List<Agricultor> listaAgricultores) {
        this.listaAgricultores = listaAgricultores;
    }

    @Override
    public AgricultorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agricultor, parent, false);
        return new AgricultorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AgricultorViewHolder holder, int position) {
        Agricultor agricultor = listaAgricultores.get(position);
        holder.bind(agricultor);
    }

    @Override
    public int getItemCount() {
        return listaAgricultores.size();
    }

    // ViewHolder para los elementos del RecyclerView
    public class AgricultorViewHolder extends RecyclerView.ViewHolder {

        private final TextView textNombre;
        private final TextView textEdad;
        private final TextView textZona;
        private final TextView textExperiencia;
        private final ImageView imageAgricultor;
        public AgricultorViewHolder(View itemView) {
            super(itemView);
            imageAgricultor = itemView.findViewById(R.id.imageAgricultor);
            textNombre = itemView.findViewById(R.id.textNombre);
            textEdad = itemView.findViewById(R.id.textEdad);
            textZona = itemView.findViewById(R.id.textZona);
            textExperiencia = itemView.findViewById(R.id.textExperiencia);
        }

        public void bind(Agricultor agricultor) {
            textNombre.setText(agricultor.getNombre() != null ? agricultor.getNombre() : "No disponible");
            textEdad.setText(String.valueOf(agricultor.getEdad()));
            textZona.setText(agricultor.getZona() != null ? agricultor.getZona() : "No disponible");
            textExperiencia.setText(agricultor.getExperiencia() != null ? agricultor.getExperiencia() : "No disponible");
            imageAgricultor.setImageResource(R.drawable.ic_person);
        }

    }
}
