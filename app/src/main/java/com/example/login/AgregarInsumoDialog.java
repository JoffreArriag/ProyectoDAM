package com.example.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AgregarInsumoDialog extends DialogFragment {

    public interface InsumoListener {
        void onInsumoAgregado(InsumoAgricola insumo);
        void onInsumoEditado(InsumoAgricola insumo, int position);
    }

    private InsumoListener listener;
    private InsumoAgricola insumoExistente;
    private int editarPos = -1;

    private Spinner spinnerNombreInsumo;
    private EditText editDescripcion, editCantidad;

    public void setInsumoListener(InsumoListener listener) {
        this.listener = listener;
    }

    public void setInsumoEditar(InsumoAgricola insumo, int position) {
        this.insumoExistente = insumo;
        this.editarPos = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_insumo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext()).setView(view);

        spinnerNombreInsumo = view.findViewById(R.id.spinnerNombreInsumo);
        editDescripcion = view.findViewById(R.id.editDescripcion);
        editCantidad = view.findViewById(R.id.editCantidad);
        Button btnGuardar = view.findViewById(R.id.btnGuardarInsumo);
        Button btnCancelar = view.findViewById(R.id.btnCancelarInsumo);

        String[] nombresInsumo = {
                "Fertilizantes",
                "Pesticidas",
                "Semillas",
                "Agua de riego",
                "Maquinaria agrícola"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombresInsumo);
        spinnerNombreInsumo.setAdapter(adapter);

        if (insumoExistente != null) {
            spinnerNombreInsumo.setSelection(adapter.getPosition(insumoExistente.getNombre()));
            editDescripcion.setText(insumoExistente.getDescripcion());
            editCantidad.setText(String.valueOf(insumoExistente.getCantidad()));
        }

        btnGuardar.setOnClickListener(this::guardarInsumo);
        btnCancelar.setOnClickListener(this::cancelar);

        return builder.create();
    }

    public void guardarInsumo(View v) {
        String nombre = spinnerNombreInsumo.getSelectedItem().toString();
        String descripcion = editDescripcion.getText().toString().trim();
        String cantidadStr = editCantidad.getText().toString().trim();

        if (descripcion.isEmpty() || cantidadStr.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            InsumoAgricola insumo = new InsumoAgricola(nombre, descripcion, cantidad);

            if (editarPos >= 0) {
                listener.onInsumoEditado(insumo, editarPos);
            } else {
                listener.onInsumoAgregado(insumo);
            }
            dismiss();
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Cantidad debe ser numérica", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelar(View v) {
        dismiss();
    }
}
