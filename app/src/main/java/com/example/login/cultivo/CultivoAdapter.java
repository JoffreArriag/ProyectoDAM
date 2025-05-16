package com.example.login.cultivo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.ArrayList;
import java.util.List;

public class CultivoAdapter extends RecyclerView.Adapter<CultivoAdapter.CultivoViewHolder> {

    private List<Cultivo> cultivos;
    private List<Cultivo> cultivosSeleccionados = new ArrayList<>();

    public CultivoAdapter(List<Cultivo> cultivos) {
        this.cultivos = cultivos;
    }

    @Override
    public CultivoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cultivo, parent, false);
        return new CultivoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CultivoViewHolder holder, int position) {
        Cultivo cultivo = cultivos.get(position);
        holder.nombre.setText(cultivo.getNombre());
        holder.categoria.setText(cultivo.getCategoria());
        holder.fechaInicio.setText(cultivo.getFechaInicio());
        holder.ubicacion.setText(cultivo.getUbicacion());
        holder.precioCaja.setText("Precio/caja: $" + cultivo.getPrecioCaja());

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(cultivosSeleccionados.contains(cultivo));

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!cultivosSeleccionados.contains(cultivo)) {
                    cultivosSeleccionados.add(cultivo);
                }
            } else {
                cultivosSeleccionados.remove(cultivo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cultivos.size();
    }

    public static class CultivoViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, categoria, fechaInicio, ubicacion, precioCaja;
        CheckBox checkbox;

        public CultivoViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreCultivo);
            categoria = itemView.findViewById(R.id.categoriaCultivo);
            fechaInicio = itemView.findViewById(R.id.fechaInicioCultivo);
            ubicacion = itemView.findViewById(R.id.ubicacionCultivo);
            checkbox = itemView.findViewById(R.id.checkboxSelect);
            precioCaja = itemView.findViewById(R.id.precioCajaCultivo);
        }
    }

    public List<Cultivo> getCultivosSeleccionados() {
        return cultivosSeleccionados;
    }
}
