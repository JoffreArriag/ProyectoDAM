package com.example.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AgregarTareaDialog extends DialogFragment {

    public interface TareaListener {
        void onTareaAgregado(Tarea tarea);
        void onTareaEditado(Tarea tarea, int position);
    }

    private TareaListener listener;
    private Tarea tareaExistente;
    private int editarPos = -1;

    public void setTareaListener(TareaListener listener) {
        this.listener = listener;
    }

    public void setTareaEditar(Tarea insumo, int position) {
        this.tareaExistente = insumo;
        this.editarPos = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_tarea, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()).setView(view);

        Spinner spinnerCultivo = view.findViewById(R.id.spinnerCultivo);
        EditText editNombre = view.findViewById(R.id.editNombre);
        EditText editDescripcion = view.findViewById(R.id.editDescripcion);
        EditText editFechaInicio = view.findViewById(R.id.txtDateInicio);
        EditText editFechaFin = view.findViewById(R.id.txtDateFin);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnCancelar = view.findViewById(R.id.btnVolver);


        String[] nombresCultivo = {
                "Cultivo de Maiz",
                "Cultivo de Cacao",

        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombresCultivo);
        spinnerCultivo.setAdapter(adapter);


        if (tareaExistente != null) {
            spinnerCultivo.setSelection(adapter.getPosition(tareaExistente.getCultivo()));
            editNombre.setText(tareaExistente.getNombre());
            editDescripcion.setText(tareaExistente.getDescripcion());
            editFechaInicio.setText(String.valueOf(tareaExistente.getFecha_inico()));
            editFechaFin.setText(String.valueOf(tareaExistente.getFecha_fin()));
        }

        btnGuardar.setOnClickListener(v -> {
            String cultivo = spinnerCultivo.getSelectedItem().toString();
            String nombre = editNombre.getText().toString().trim();
            String descripcion = editDescripcion.getText().toString().trim();
            String fecha_inico = editFechaInicio.getText().toString().trim();
            String fecha_fin = editFechaFin.getText().toString().trim();

            if (descripcion.isEmpty() || nombre.isEmpty() || cultivo.isEmpty() || fecha_fin.isEmpty() || fecha_inico.isEmpty() ) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                Tarea tarea = new Tarea(nombre, descripcion, cultivo,fecha_inico, fecha_fin );

                if (editarPos >= 0) {
                    listener.onTareaEditado(tarea, editarPos);
                } else {
                    listener.onTareaAgregado(tarea);
                }
                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Cantidad debe ser numérica", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> dismiss());

        return builder.create();
    }
}
