package com.example.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AgregarAgricultorDialog extends DialogFragment {

    public interface AgricultorListener {
        void onAgricultorAgregado(Agricultor agricultor);
    }

    private AgricultorListener listener;

    public void setAgricultorListener(AgricultorListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_agregar_agricultor, null);

        EditText editNombre = view.findViewById(R.id.editNombre);
        EditText editEdad = view.findViewById(R.id.editEdad);
        EditText editZona = view.findViewById(R.id.editZona);
        EditText editExperiencia = view.findViewById(R.id.editExperiencia);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnVolver = view.findViewById(R.id.btnVolver);

        builder.setView(view);

        Dialog dialog = builder.create();

        btnGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String edadStr = editEdad.getText().toString().trim();
            String zona = editZona.getText().toString().trim();
            String experiencia = editExperiencia.getText().toString().trim();

            if (nombre.isEmpty() || edadStr.isEmpty() || zona.isEmpty() || experiencia.isEmpty()) {
                Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int edad = Integer.parseInt(edadStr);

                Agricultor nuevoAgricultor = new Agricultor(nombre, edad, zona, experiencia);
                if (listener != null) {
                    listener.onAgricultorAgregado(nuevoAgricultor);
                }
                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Edad debe ser un número", Toast.LENGTH_SHORT).show();
            }
        });


        btnVolver.setOnClickListener(v -> dismiss());

        return dialog;
    }
}
