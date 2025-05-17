package com.example.login.agricultor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.cultivo.Cultivo;

import java.util.ArrayList;
import java.util.List;

public class AgricultoresAdapter extends RecyclerView.Adapter<AgricultoresAdapter.AgricultorViewHolder> {

    private List<Agricultor> listaAgricultores;
  private List<Agricultor> agricultorSeleccionados = new ArrayList<>();

    public AgricultoresAdapter(List<Agricultor> agricultores) {
        this.listaAgricultores = agricultores;
    }
    private  OnAgricultorAccionListener accionListener;

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


    public void actualizarLista(List<Agricultor> nuevaLista) {
        this.listaAgricultores = nuevaLista;
        notifyDataSetChanged();
    }

    public class AgricultorViewHolder extends RecyclerView.ViewHolder {

        TextView textNombre, textEdad, textZona, textExperiencia;
        ImageView imageAgricultor, btnEditar, btnEliminar;
        Agricultor agricultorActual;
        CheckBox agricultorSeleccionado;
        public AgricultorViewHolder(View itemView) {
            super(itemView);
            imageAgricultor = itemView.findViewById(R.id.imageAgricultor);
            textNombre = itemView.findViewById(R.id.textNombre);
            textEdad = itemView.findViewById(R.id.textEdad);
            textZona = itemView.findViewById(R.id.textZona);
            textExperiencia = itemView.findViewById(R.id.textExperiencia);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            agricultorSeleccionado = itemView.findViewById(R.id.checkAgricultorTarea);

            agricultorSeleccionado.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (agricultorActual != null) {
                    if (isChecked) {
                        if (!agricultorSeleccionados.contains(agricultorActual)) {
                            agricultorSeleccionados.add(agricultorActual);
                        }
                    } else {
                        agricultorSeleccionados.remove(agricultorActual);
                    }
                }
            });

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
            agricultorSeleccionado.setChecked(agricultorSeleccionados.contains(agricultor));

            // Mostrar/ocultar según si hay listener
            if (accionListener != null) {
                btnEditar.setVisibility(View.VISIBLE);
                btnEliminar.setVisibility(View.VISIBLE);
                agricultorSeleccionado.setVisibility(View.GONE);
            } else {
                btnEditar.setVisibility(View.GONE);
                btnEliminar.setVisibility(View.GONE);
                agricultorSeleccionado.setVisibility(View.VISIBLE);
            }
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
    }
    public List<Agricultor> getAgricultoresSeleccionados() {
        return agricultorSeleccionados;
    }
}
