package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AgricultoresAdapter extends RecyclerView.Adapter<AgricultoresAdapter.AgricultorViewHolder> {

    private final List<Agricultor> listaAgricultores;
    private final OnAgricultorAccionListener accionListener;

    public interface OnAgricultorAccionListener {
        void onEditar(Agricultor agricultor, int position);
        void onEliminar(int position);
    }

    public AgricultoresAdapter(List<Agricultor> listaAgricultores, OnAgricultorAccionListener listener) {
        this.listaAgricultores = listaAgricultores;
        this.accionListener = listener;
    }

    @Override
    public AgricultorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agricultor, parent, false);
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

    public class AgricultorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textNombre, textEdad, textZona, textExperiencia;
        ImageView imageAgricultor, btnEditar, btnEliminar;
        Agricultor agricultorActual;

        public AgricultorViewHolder(View itemView) {
            super(itemView);
            imageAgricultor = itemView.findViewById(R.id.imageAgricultor);
            textNombre = itemView.findViewById(R.id.textNombre);
            textEdad = itemView.findViewById(R.id.textEdad);
            textZona = itemView.findViewById(R.id.textZona);
            textExperiencia = itemView.findViewById(R.id.textExperiencia);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);

            btnEditar.setOnClickListener(this::editarAgricultor);
            btnEliminar.setOnClickListener(this::eliminarAgricultor);
        }

        public void bind(Agricultor agricultor) {
            agricultorActual = agricultor;
            textNombre.setText(agricultor.getNombre());
            textEdad.setText(String.valueOf(agricultor.getEdad()));
            textZona.setText(agricultor.getZona());
            textExperiencia.setText(agricultor.getExperiencia());
            imageAgricultor.setImageResource(R.drawable.ic_person);
        }

        public void editarAgricultor(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && agricultorActual != null) {
                accionListener.onEditar(agricultorActual, position);
            }
        }

        public void eliminarAgricultor(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                accionListener.onEliminar(position);
            }
        }

        @Override
        public void onClick(View v) {

        }
    }
}
