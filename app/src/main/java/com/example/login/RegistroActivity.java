package com.example.login;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;

import androidx.appcompat.app.AppCompatActivity;

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

        BDOpenHelper dbHelper = new BDOpenHelper(this);

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


        btnSeleccionarFecha.setOnClickListener(view -> {
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
        });

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
            //Guardar en BD
            if(guardarBD(vCedula, vNombres,vApellidos,vEdad,vNacionalidad,vGenero,vEstadoCivil,vFechaNacimiento,vNivelIngles )){
                Toast.makeText(this, "Datos registrados correctamente", Toast.LENGTH_LONG).show();
            }else{

            }


        });

        // Botón Borrar
        btnBorrar.setOnClickListener(view -> {
            cedula.setText("");
            nombres.setText("");
            apellidos.setText("");
            edad.setText("");
            radioGroupEstadoCivil.clearCheck();
            txtFechaSeleccionada.setText("Fecha no seleccionada");
            ratingIngles.setRating(0);
            spinnerNacionalidad.setSelection(0);
            spinnerGenero.setSelection(0);
        });

        // Botón Cancelar
        btnCancelar.setOnClickListener(view -> {
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Botón Mostrar Datos
        btnMostrarDatos.setOnClickListener(view -> {
            EditText inputBusqueda = new EditText(RegistroActivity.this);
            inputBusqueda.setHint("Ingrese cédula o nombre");

            new androidx.appcompat.app.AlertDialog.Builder(RegistroActivity.this)
                    .setTitle("Buscar Usuario")
                    .setView(inputBusqueda)
                    .setPositiveButton("Buscar", (dialog, which) -> {
                        String criterio = inputBusqueda.getText().toString().trim();

                        if (criterio.isEmpty()) {
                            Toast.makeText(this, "Debe ingresar cédula o nombre", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        SQLiteDatabase db = dbHelper.getReadableDatabase();
                        String query = "SELECT * FROM usuarios WHERE cedula = ? OR nombres LIKE ?";
                        try (android.database.Cursor cursor = db.rawQuery(query, new String[]{criterio, "%" + criterio + "%"})) {
                            if (cursor.moveToFirst()) {
                                String mensaje = "Cédula: " + cursor.getString(0) + "\n" +
                                        "Nombres: " + cursor.getString(1) + "\n" +
                                        "Apellidos: " + cursor.getString(2) + "\n" +
                                        "Edad: " + cursor.getString(3) + "\n" +
                                        "Nacionalidad: " + cursor.getString(4) + "\n" +
                                        "Género: " + cursor.getString(5) + "\n" +
                                        "Estado Civil: " + cursor.getString(6) + "\n" +
                                        "Fecha Nacimiento: " + cursor.getString(7) + "\n" +
                                        "Nivel Inglés: " + cursor.getFloat(8);

                                new androidx.appcompat.app.AlertDialog.Builder(RegistroActivity.this)
                                        .setTitle("Datos encontrados")
                                        .setMessage(mensaje)
                                        .setPositiveButton("Cerrar", null)
                                        .show();
                            } else {
                                new androidx.appcompat.app.AlertDialog.Builder(RegistroActivity.this)
                                        .setTitle("Sin resultados")
                                        .setMessage("No existen datos con esa cédula o nombre.")
                                        .setPositiveButton("Cerrar", null)
                                        .show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Error en la búsqueda", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }
    public boolean guardarBD(String cedula, String nombres, String apellidos,
                             String edad, String nacionalidad, String genero, String estadoCivil,
                             String fechaNacimiento, float ratingIngles) {
        BDOpenHelper dbAgricola = new BDOpenHelper(this);
        final SQLiteDatabase dbAgricolaEdit = dbAgricola.getWritableDatabase();
        if(dbAgricolaEdit != null){
            ContentValues valores = new ContentValues();
            valores.put("cedula", cedula);
            valores.put("nombres", nombres);
            valores.put("apellidos", apellidos);
            valores.put("edad", edad);
            valores.put("nacionalidad", nacionalidad);
            valores.put("genero", genero);
            valores.put("estado_civil", estadoCivil);
            valores.put("fecha_nacimiento", fechaNacimiento);
            valores.put("ratingIngles", ratingIngles);
            dbAgricolaEdit.insert("usuario", null, valores);
            return true;
        }else{
            return false;
        }

    }

}
