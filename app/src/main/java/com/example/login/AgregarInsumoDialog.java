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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_agregar_insumo, null);

        spinnerNombreInsumo = view.findViewById(R.id.spinnerNombreInsumo);
        editDescripcion = view.findViewById(R.id.editDescripcion);
        editCantidad = view.findViewById(R.id.editCantidad);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.opciones_insumos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNombreInsumo.setAdapter(adapter);

        if (insumoExistente != null) {
            spinnerNombreInsumo.setSelection(adapter.getPosition(insumoExistente.getNombre()));
            editDescripcion.setText(insumoExistente.getDescripcion());
            editCantidad.setText(String.valueOf(insumoExistente.getCantidad()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(insumoExistente == null ? "Agregar Insumo" : "Editar Insumo");
        builder.setView(view);

        AlertDialog dialog = builder.create();

        Button btnGuardar = view.findViewById(R.id.btnGuardarInsumo);
        Button btnCancelar = view.findViewById(R.id.btnCancelarInsumo);

        btnGuardar.setOnClickListener(v -> {
            String nombre = spinnerNombreInsumo.getSelectedItem().toString();
            String descripcion = editDescripcion.getText().toString();
            String cantidadTexto = editCantidad.getText().toString();

            if (cantidadTexto.isEmpty()) {
                editCantidad.setError("Ingrese una cantidad");
                return;
            }

            int cantidad;
            try {
                cantidad = Integer.parseInt(cantidadTexto);
            } catch (NumberFormatException e) {
                editCantidad.setError("Cantidad inválida");
                return;
            }

            if (insumoExistente == null) {
                InsumoAgricola nuevo = new InsumoAgricola(nombre, descripcion, cantidad);
                if (listener != null) listener.onInsumoAgregado(nuevo);
            } else {
                insumoExistente.setNombre(nombre);
                insumoExistente.setDescripcion(descripcion);
                insumoExistente.setCantidad(cantidad);
                if (listener != null) listener.onInsumoEditado(insumoExistente, editarPos);
            }
            dialog.dismiss();
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }

}
