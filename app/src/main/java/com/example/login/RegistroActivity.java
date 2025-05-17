package com.example.login;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegistroActivity extends AppCompatActivity {

    EditText cedula, nombres, apellidos, edad;
    Spinner spinnerNacionalidad, spinnerGenero;
    RadioGroup radioGroupEstadoCivil;
    TextView txtFechaSeleccionada;
    RatingBar ratingIngles;

    Button btnSeleccionarFecha, btnRegistrar, btnBorrar, btnCancelar, btnMostrarDatos;

    String fechaNacimiento = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_registro);

        // Referencias a vistas
        cedula = findViewById(R.id.cedula);
        nombres = findViewById(R.id.nombres);
        apellidos = findViewById(R.id.apellidos);
        edad = findViewById(R.id.edad);

        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        radioGroupEstadoCivil = findViewById(R.id.radioGroupEstadoCivil);
        txtFechaSeleccionada = findViewById(R.id.txtFechaSeleccionada);
        ratingIngles = findViewById(R.id.ratingIngles);

        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnMostrarDatos = findViewById(R.id.btnMostrarDatos);

        String[] nacionalidades = {"Seleccione nacionalidad", "Ecuatoriana", "Colombiana", "Peruana", "Venezolana"};
        ArrayAdapter<String> adapterNacionalidad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nacionalidades);
        spinnerNacionalidad.setAdapter(adapterNacionalidad);

        String[] generos = {"Seleccione género", "Masculino", "Femenino", "Otro"};
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos);
        spinnerGenero.setAdapter(adapterGenero);

        // Botón Registrar
        btnRegistrar.setOnClickListener(view -> {
            String vCedula = cedula.getText().toString();
            String vNombres = nombres.getText().toString();
            String vApellidos = apellidos.getText().toString();
            String vEdad = edad.getText().toString();
            String vNacionalidad = spinnerNacionalidad.getSelectedItem().toString();
            String vGenero = spinnerGenero.getSelectedItem().toString();
            String vEstadoCivil = "";

            int idSeleccionado = radioGroupEstadoCivil.getCheckedRadioButtonId();
            if (idSeleccionado != -1) {
                RadioButton rb = findViewById(idSeleccionado);
                vEstadoCivil = rb.getText().toString();
            }

            String vFechaNacimiento = fechaNacimiento;
            float vNivelIngles = ratingIngles.getRating();

            if (vNacionalidad.equals("Seleccione nacionalidad") || vGenero.equals("Seleccione género")) {
                Toast.makeText(this, "Debe seleccionar una nacionalidad y un género válidos", Toast.LENGTH_LONG).show();
                return;
            }

            if (vCedula.isEmpty() || vNombres.isEmpty() || vApellidos.isEmpty() || vEdad.isEmpty()) {
                Toast.makeText(this, "Debe llenar todos los campos obligatorios", Toast.LENGTH_LONG).show();
                return;
            }

            guardarEnFirebase(vCedula, vNombres, vApellidos, vEdad, vNacionalidad, vGenero, vEstadoCivil, vFechaNacimiento, vNivelIngles);
        });
    }

    // Método para guardar en Firebase
    public void guardarEnFirebase(String cedula, String nombres, String apellidos,
                                  String edad, String nacionalidad, String genero,
                                  String estadoCivil, String fechaNacimiento, float ratingIngles) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("usuarios");

        Usuario usuario = new Usuario(cedula, nombres, apellidos, edad,
                nacionalidad, genero, estadoCivil, fechaNacimiento, ratingIngles);

        ref.child(cedula).setValue(usuario)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Datos guardados en Firebase", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar en Firebase", Toast.LENGTH_SHORT).show());
    }

    public void borrarCampos(View v) {
        cedula.setText("");
        nombres.setText("");
        apellidos.setText("");
        edad.setText("");
        radioGroupEstadoCivil.clearCheck();
        txtFechaSeleccionada.setText("Fecha no seleccionada");
        ratingIngles.setRating(0);
        spinnerNacionalidad.setSelection(0);
        spinnerGenero.setSelection(0);
    }

    public void cancelarRegistro(View v) {
        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void mostrarDatosBD(View v) {
        Intent intent = new Intent(RegistroActivity.this, ConsultarUsuario.class);
        startActivity(intent);
    }

    public void seleccionarFecha(View v) {
        Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker = new DatePickerDialog(
                RegistroActivity.this,
                (view1, year, month, dayOfMonth) -> {
                    fechaNacimiento = dayOfMonth + "/" + (month + 1) + "/" + year;
                    txtFechaSeleccionada.setText(fechaNacimiento);
                },
                año, mes, dia
        );
        datePicker.show();
    }
}
