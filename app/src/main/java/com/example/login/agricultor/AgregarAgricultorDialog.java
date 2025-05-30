package com.example.login.agricultor;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.login.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AgregarAgricultorDialog extends DialogFragment {

    public interface AgricultorListener {
        void onAgricultorAgregado(Agricultor agricultor);
        void onAgricultorEditado(Agricultor agricultor, int position);
    }

    private AgricultorListener listener;
    private Agricultor agricultorExistente;
    private int editarPos = -1;

    private EditText editNombre;
    private EditText editEdad;
    private EditText editZona;
    private Spinner spinnerExperiencia;

    private DatabaseReference agricultoresRef;

    public void setAgricultorListener(AgricultorListener listener) {
        this.listener = listener;
    }

    public void setAgricultorEditar(Agricultor agricultor, int position) {
        this.agricultorExistente = agricultor;
        this.editarPos = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_agricultor, null);
        builder.setView(view);

        editNombre = view.findViewById(R.id.editNombre);
        editEdad = view.findViewById(R.id.editEdad);
        editZona = view.findViewById(R.id.editZona);
        spinnerExperiencia = view.findViewById(R.id.spinnerExperiencia);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnVolver = view.findViewById(R.id.btnVolver);

        String[] opciones = {
                "Cuidado de cultivos",
                "Manejo de maquinaria",
                "Gestión de recursos agrícolas",
                "Venta y comercialización de productos"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, opciones);
        spinnerExperiencia.setAdapter(adapter);

        if (agricultorExistente != null) {
            editNombre.setText(agricultorExistente.getNombre());
            editEdad.setText(String.valueOf(agricultorExistente.getEdad()));
            editZona.setText(agricultorExistente.getZona());
            spinnerExperiencia.setSelection(adapter.getPosition(agricultorExistente.getExperiencia()));
        }


        agricultoresRef = FirebaseDatabase.getInstance().getReference("agricultores");

        btnGuardar.setOnClickListener(this::guardarAgricultor);
        btnVolver.setOnClickListener(v -> dismiss());

        return builder.create();
    }

    public void guardarAgricultor(View v) {
        String nombre = editNombre.getText().toString().trim();
        String edadStr = editEdad.getText().toString().trim();
        String zona = editZona.getText().toString().trim();
        String experiencia = spinnerExperiencia.getSelectedItem().toString();

        if (nombre.isEmpty() || edadStr.isEmpty() || zona.isEmpty()) {
            Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int edad = Integer.parseInt(edadStr);
            String id = agricultorExistente != null ? agricultorExistente.getId() : UUID.randomUUID().toString();
            Agricultor nuevo = new Agricultor(id, nombre, edad, zona, experiencia);

            if (editarPos >= 0 && listener != null) {

                agricultoresRef.child(id).setValue(nuevo)
                        .addOnSuccessListener(task -> {
                            listener.onAgricultorEditado(nuevo, editarPos);
                            dismiss();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Error al editar agricultor", Toast.LENGTH_SHORT).show()
                        );
            } else {

                agricultoresRef.child(id).setValue(nuevo)
                        .addOnSuccessListener(task -> {
                            listener.onAgricultorAgregado(nuevo);
                            dismiss();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Error al guardar agricultor", Toast.LENGTH_SHORT).show()
                        );
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Edad debe ser numérica", Toast.LENGTH_SHORT).show();
        }
    }
}
