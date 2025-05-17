package com.example.login.cultivo;

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

import com.example.login.R;
import com.example.login.agricultor.Agricultor;
import com.example.login.mercado.MercadoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AgregarCultivoDialog extends DialogFragment {

    private EditText etNombre, etFecha, etUbicacion, etPrecioCaja;
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
        etPrecioCaja = view.findViewById(R.id.etPrecioCaja);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnVolver = view.findViewById(R.id.btnVolver);

        String[] categorias = {"Cereales", "Leguminosas", "Industriales", "Hortalizas", "Frutales"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        if (cultivoExistente != null) {
            etNombre.setText(cultivoExistente.getNombre());
            etFecha.setText(cultivoExistente.getFechaInicio());
            etUbicacion.setText(cultivoExistente.getUbicacion());
            etPrecioCaja.setText(String.valueOf(cultivoExistente.getPrecioCaja()));
            int position = adapter.getPosition(cultivoExistente.getCategoria());
            spinnerCategoria.setSelection(position);
        }

        etFecha.setOnClickListener(this::mostrarDatePicker);
        btnGuardar.setOnClickListener(this::guardarCultivo);
        btnVolver.setOnClickListener(this::volver);

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
    }

    public void mostrarDatePicker(View v) {
        Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(requireContext(), (view1, year, month, day) -> {
            String fecha = day + "/" + (month + 1) + "/" + year;
            etFecha.setText(fecha);
        }, año, mes, dia);
        datePicker.show();
    }


    public void guardarCultivo(View v) {
        String nombre = etNombre.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String precioStr = etPrecioCaja.getText().toString().trim();

        if (nombre.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Cultivo cultivo = new Cultivo(nombre, categoria, fecha, ubicacion, precio);

        if (listener != null) {
            listener.onCultivoAgregado(cultivo);
        }

        dismiss();
    }

    public void volver(View v) {
        dismiss();
    }
}
