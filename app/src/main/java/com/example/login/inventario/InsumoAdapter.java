package com.example.login.inventario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.cultivo.Cultivo;

import java.util.ArrayList;
import java.util.List;

public class InsumoAdapter extends RecyclerView.Adapter<InsumoAdapter.ViewHolder> {

    public interface OnInsumoAccionListener {
        void onEditar(InsumoAgricola insumo, int position);
        void onEliminar(int position);
    }
    private List<InsumoAgricola> insumoSeleccionados = new ArrayList<>();
    private List<InsumoAgricola> lista;
    private OnInsumoAccionListener listener;

    public InsumoAdapter(List<InsumoAgricola> lista, OnInsumoAccionListener listener) {
        this.lista = lista;
        this.listener = listener;
    }
    public InsumoAdapter(List<InsumoAgricola> lista) {
        this.lista = lista;

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textNombre, textDescripcion, textCantidad;
        ImageView btnEditar, btnEliminar;
        CheckBox checkInsumo;
        InsumoAgricola insumoActual;


        public ViewHolder(View view) {
            super(view);
            textNombre = view.findViewById(R.id.textNombre);
            textDescripcion = view.findViewById(R.id.textDescripcion);
            textCantidad = view.findViewById(R.id.textCantidad);
            btnEditar = view.findViewById(R.id.btnEditar);
            btnEliminar = view.findViewById(R.id.btnEliminar);
            checkInsumo = view.findViewById(R.id.checkInsumos);
            checkInsumo.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (insumoActual != null) {
                    if (isChecked) {
                        if (!insumoSeleccionados.contains(insumoActual)) {
                            insumoSeleccionados.add(insumoActual);
                        }
                    } else {
                        insumoSeleccionados.remove(insumoActual);
                    }
                }
            });
        }

        public void bind(InsumoAgricola insumo, int position, OnInsumoAccionListener listener) {
            textNombre.setText(insumo.getNombre());
            textDescripcion.setText(insumo.getDescripcion());
            textCantidad.setText("Cantidad: " + insumo.getCantidad());
            insumoActual = insumo;
            int imagenId = InsumoAdapter.obtenerImagenPorNombre(insumo.getNombre());

            ImageView icono = itemView.findViewById(R.id.imageInsumo);
            icono.setImageResource(imagenId);
            checkInsumo.setChecked(insumoSeleccionados.contains(insumo));
            // Mostrar/ocultar según si hay listener
            if (listener != null) {
                btnEditar.setVisibility(View.VISIBLE);
                btnEliminar.setVisibility(View.VISIBLE);
                checkInsumo.setVisibility(View.GONE);
            } else {
                btnEditar.setVisibility(View.GONE);
                btnEliminar.setVisibility(View.GONE);
                checkInsumo.setVisibility(View.VISIBLE);
            }
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
    public List<InsumoAgricola> getInsumoSeleccionados() {
        return insumoSeleccionados;
    }
}
