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


    public static int obtenerImagenPorNombre(String nombre) {
        if (nombre == null) return R.drawable.ic_inventar;

        switch (nombre.trim().toLowerCase()) {
            case "fertilizantes":
                return R.drawable.ic_fertilizante;
            case "pesticidas":
                return R.drawable.ic_pesticida;
            case "semillas":
                return R.drawable.ic_semilla;
            case "agua de riego":
                return R.drawable.ic_agua;
            case "maquinaria agrícola":
                return R.drawable.ic_maquinaria;
            default:
                return R.drawable.ic_inventar;
        }
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

            int imagenId = InsumoAdapter.obtenerImagenPorNombre(insumo.getNombre());

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
