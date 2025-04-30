package com.example.login;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class CultivosActivity extends AppCompatActivity {

    private ArrayList<String> listaCultivos;
    private ArrayAdapter<String> adaptador;
    private int cultivoEditando = -1;

    // Elementos del formulario
    private LinearLayout layoutFormulario;
    private EditText etTipo, etFecha, etUbicacion;
    private Button btnGuardar, btnCancelar;
    private ImageButton btnAgregar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivos);

        // Referencias
        layoutFormulario = findViewById(R.id.layoutFormularioCultivo);
        etTipo = findViewById(R.id.etTipo);
        etFecha = findViewById(R.id.etFecha);
        etUbicacion = findViewById(R.id.etUbicacion);
        btnGuardar = findViewById(R.id.btnGuardarCultivo);
        btnCancelar = findViewById(R.id.btnCancelarCultivo);
        btnAgregar = findViewById(R.id.btnAgregar);
        listView = findViewById(R.id.listViewCultivos);

        listaCultivos = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCultivos);
        listView.setAdapter(adaptador);

        // Mostrar formulario al presionar "Agregar"
        btnAgregar.setOnClickListener(v -> {
            cultivoEditando = -1;
            mostrarFormulario();
        });

        // Guardar cultivo
        btnGuardar.setOnClickListener(v -> guardarCultivo());

        // Cancelar acción
        btnCancelar.setOnClickListener(v -> ocultarFormulario());

        // Elegir fecha
        etFecha.setOnClickListener(v -> mostrarDatePicker());

        // Clic en item para editar o eliminar
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Se actualiza la comparación con el operador correcto
            mostrarOpciones(position);
        });
    }

    private void mostrarFormulario() {
        layoutFormulario.setVisibility(View.VISIBLE);
        etTipo.setText("");
        etFecha.setText("");
        etUbicacion.setText("");
    }

    private void ocultarFormulario() {
        layoutFormulario.setVisibility(View.GONE);
    }

    private void guardarCultivo() {
        String tipo = etTipo.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();

        if (tipo.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String cultivo = "Tipo: " + tipo + " | Fecha: " + fecha + " | Ubicación: " + ubicacion;

        if (cultivoEditando == -1) {
            listaCultivos.add(cultivo);
            Toast.makeText(this, "Cultivo agregado", Toast.LENGTH_SHORT).show();
        } else {
            listaCultivos.set(cultivoEditando, cultivo);
            Toast.makeText(this, "Cultivo actualizado", Toast.LENGTH_SHORT).show();
            cultivoEditando = -1;
        }

        adaptador.notifyDataSetChanged();
        ocultarFormulario();
    }

    private void mostrarOpciones(int position) {
        // Corregir el uso de AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones")
                .setItems(new CharSequence[]{"Editar", "Eliminar"}, (dialog, which) -> {
                    // La comparación de la opción elegida se hace de la siguiente manera
                    if (which == 0) {
                        editarCultivo(position);
                    } else if (which == 1) {
                        eliminarCultivo(position);
                    }
                }).show();
    }

    private void editarCultivo(int position) {
        String cultivo = listaCultivos.get(position);
        String[] partes = cultivo.split("\\|");
        if (partes.length == 3) {
            etTipo.setText(partes[0].replace("Tipo:", "").trim());
            etFecha.setText(partes[1].replace("Fecha:", "").trim());
            etUbicacion.setText(partes[2].replace("Ubicación:", "").trim());
            cultivoEditando = position;
            mostrarFormulario();
        }
    }

    private void eliminarCultivo(int position) {
        listaCultivos.remove(position);
        adaptador.notifyDataSetChanged();
        Toast.makeText(this, "Cultivo eliminado", Toast.LENGTH_SHORT).show();
    }

    private void mostrarDatePicker() {
        final Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int día = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) ->
                        etFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                año, mes, día);
        datePickerDialog.show();
    }
}
