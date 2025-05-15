package com.example.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class InsumoAdapter extends RecyclerView.Adapter<InsumoAdapter.ViewHolder> {

    public interface OnInsumoAccionListener {
        void onEditar(InsumoAgricola insumo, int position);
        void onEliminar(int position);
    }

    private List<InsumoAgricola> lista;
    private OnInsumoAccionListener listener;

    public InsumoAdapter(List<InsumoAgricola> lista, OnInsumoAccionListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textDescripcion, textCantidad;
        ImageView btnEditar, btnEliminar;

        public ViewHolder(View view) {
            super(view);
            textNombre = view.findViewById(R.id.textNombre);
            textDescripcion = view.findViewById(R.id.textDescripcion);
            textCantidad = view.findViewById(R.id.textCantidad);
            btnEditar = view.findViewById(R.id.btnEditar);
            btnEliminar = view.findViewById(R.id.btnEliminar);
        }

        public void bind(InsumoAgricola insumo, int position, OnInsumoAccionListener listener) {
            textNombre.setText(insumo.getNombre());
            textDescripcion.setText(insumo.getDescripcion());
            textCantidad.setText("Cantidad: " + insumo.getCantidad());

            int imagenId = R.drawable.ic_inventar;
            switch (insumo.getNombre()) {
                case "Fertilizantes": imagenId = R.drawable.ic_fertilizante; break;
                case "Pesticidas": imagenId = R.drawable.ic_pesticida; break;
                case "Semillas": imagenId = R.drawable.ic_semilla; break;
                case "Agua de riego": imagenId = R.drawable.ic_agua; break;
                case "Maquinaria agrícola": imagenId = R.drawable.ic_maquinaria; break;
            }

            ImageView icono = itemView.findViewById(R.id.imageInsumo);
            icono.setImageResource(imagenId);

            btnEditar.setOnClickListener(v -> listener.onEditar(insumo, position));
            btnEliminar.setOnClickListener(v -> listener.onEliminar(position));
        }
    }

    @Override
    public InsumoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_insumo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InsumoAdapter.ViewHolder holder, int position) {
        holder.bind(lista.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<InsumoAgricola> nuevaLista) {
        lista = nuevaLista;
        notifyDataSetChanged();
    }
}
