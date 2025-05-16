package com.example.login.tarea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {
    private final List<Tarea> listaTarea;
    private final TareaAdapter.OnTareaAccionListener accionListener;
    public interface OnTareaAccionListener {
        void onEditar(Tarea tarea, int position);
        void onEliminar(int position);
    }

    public TareaAdapter(List<Tarea> listaTarea, TareaAdapter.OnTareaAccionListener listener) {
        this.listaTarea = listaTarea;
        this.accionListener = listener;
    }

    @Override
    public TareaAdapter.TareaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaAdapter.TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TareaAdapter.TareaViewHolder holder, int position) {
        Tarea tarea = listaTarea.get(position);
        holder.bind(tarea, position);
    }

    @Override
    public int getItemCount() {
        return listaTarea.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {

        TextView textNombre, textCultivo, textFechaInicio, textFechaFin;
        ImageView imageTarea, btnEditar, btnEliminar;

        public TareaViewHolder(View itemView) {
            super(itemView);
            imageTarea = itemView.findViewById(R.id.imageTarea);
            textNombre = itemView.findViewById(R.id.textNombre);
            textCultivo = itemView.findViewById(R.id.textCultivo);
            textFechaInicio = itemView.findViewById(R.id.textFechaInicio);
            textFechaFin = itemView.findViewById(R.id.textFechaFin);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bind(Tarea tarea, int position) {
            textNombre.setText(tarea.getNombre());
            textCultivo.setText(String.valueOf(tarea.getCultivo()));
            textFechaInicio.setText(tarea.getFecha_inico());
            textFechaFin.setText(tarea.getFecha_fin());
            imageTarea.setImageResource(R.drawable.ic_inventar);

            btnEditar.setOnClickListener(v -> accionListener.onEditar(tarea, position));
            btnEliminar.setOnClickListener(v -> accionListener.onEliminar(position));
        }
    }
}
