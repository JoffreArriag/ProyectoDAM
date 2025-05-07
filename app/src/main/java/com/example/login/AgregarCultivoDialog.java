package com.example.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class AgregarCultivoDialog extends DialogFragment {

    private EditText etNombre, etFecha, etUbicacion;
    private Spinner spinnerCategoria;
    private Button btnGuardar, btnVolver;

    public interface CultivoListener {
        void onCultivoAgregado(Cultivo cultivo);
    }

    private CultivoListener listener;

    private Cultivo cultivoExistente;

    public void setCultivo(Cultivo cultivo) {
        this.cultivoExistente = cultivo;
    }

    public void setCultivoListener(CultivoListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_agregar_cultivo, null);

        etNombre = view.findViewById(R.id.etNombreCultivo);
        etFecha = view.findViewById(R.id.etFechaInicio);
        etUbicacion = view.findViewById(R.id.etUbicacion);
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnVolver = view.findViewById(R.id.btnVolver);

        // Spinner opciones
        String[] categorias = {"Cereales", "Leguminosas", "Industriales", "Hortalizas", "Frutales"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);


        if (cultivoExistente != null) {
            etNombre.setText(cultivoExistente.getNombre());
            etFecha.setText(cultivoExistente.getFechaInicio());
            etUbicacion.setText(cultivoExistente.getUbicacion());

            int position = adapter.getPosition(cultivoExistente.getCategoria());
            spinnerCategoria.setSelection(position);
        }


        // Fecha con DatePicker
        etFecha.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(requireContext(), (view1, year, month, day) -> {
                String fecha = day + "/" + (month + 1) + "/" + year;
                etFecha.setText(fecha);
            }, año, mes, dia);
            datePicker.show();
        });

        // Botón guardar
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String fecha = etFecha.getText().toString();
            String ubicacion = etUbicacion.getText().toString();
            String categoria = spinnerCategoria.getSelectedItem().toString();

            if (!nombre.isEmpty() && !fecha.isEmpty() && !ubicacion.isEmpty()) {
                Cultivo cultivo = new Cultivo(nombre, categoria, fecha, ubicacion);

                BDOpenHelper dbHelper = new BDOpenHelper(getContext());
                boolean insertado = dbHelper.insertarCultivo(cultivo);

                if (insertado) {
                    if (listener != null) {
                        listener.onCultivoAgregado(cultivo);
                    }
                    Toast.makeText(getContext(), "Cultivo guardado correctamente", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Error al guardar el cultivo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón volver
        btnVolver.setOnClickListener(v -> dismiss());


        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
    }
}
