package com.example.login;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;

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

        // Spinners: valores ejemplo
        String[] nacionalidades = {"Seleccione nacionalidad", "Ecuatoriana", "Colombiana", "Peruana", "Venezolana"};
        ArrayAdapter<String> adapterNacionalidad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nacionalidades);
        spinnerNacionalidad.setAdapter(adapterNacionalidad);

        String[] generos = {"Seleccione género", "Masculino", "Femenino", "Otro"};
        ArrayAdapter<String> adapterGenero = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos);
        spinnerGenero.setAdapter(adapterGenero);

        // Fecha nacimiento con Picker
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
            // Guardar cada dato en variable
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

            // Validaciones antes de guardar
            if (vNacionalidad.equals("Seleccione nacionalidad") || vGenero.equals("Seleccione género")) {
                Toast.makeText(this, "Debe seleccionar una nacionalidad y un género válidos", Toast.LENGTH_LONG).show();
                return;
            }

            if (vCedula.isEmpty() || vNombres.isEmpty() || vApellidos.isEmpty() || vEdad.isEmpty()) {
                Toast.makeText(this, "Debe llenar todos los campos obligatorios", Toast.LENGTH_LONG).show();
                return;
            }

            // Crear string con separadores
            String datos = vCedula + ";" +
                    vNombres + ";" +
                    vApellidos + ";" +
                    vEdad + ";" +
                    vNacionalidad + ";" +
                    vGenero + ";" +
                    vEstadoCivil + ";" +
                    vFechaNacimiento + ";" +
                    vNivelIngles + ";";

            //Guardar en archivo
            try {
                openFileOutput("datos.txt", MODE_PRIVATE).write(datos.getBytes());
                Toast.makeText(this, "Datos registrados correctamente", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_LONG).show();
            }

            //guardar en la BD
            if(guardarBD(vCedula.toString(), vNombres.toString(), vNivelIngles)){

            }


            // Volver al login
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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

        //Boton Mostrar Datos
        btnMostrarDatos.setOnClickListener(view -> {
            try {
                // Leer archivo
                byte[] buffer = new byte[openFileInput("datos.txt").available()];
                openFileInput("datos.txt").read(buffer);
                String contenido = new String(buffer);

                // Separar los datos
                String[] partes = contenido.split(";");
                String mensaje = "Cédula: " + partes[0] + "\n" +
                        "Nombres: " + partes[1] + "\n" +
                        "Apellidos: " + partes[2] + "\n" +
                        "Edad: " + partes[3] + "\n" +
                        "Nacionalidad: " + partes[4] + "\n" +
                        "Género: " + partes[5] + "\n" +
                        "Estado Civil: " + partes[6] + "\n" +
                        "Fecha Nacimiento: " + partes[7] + "\n" +
                        "Nivel de Inglés: " + partes[8];

                // Mostrar en ventana modal (AlertDialog)
                new androidx.appcompat.app.AlertDialog.Builder(RegistroActivity.this)
                        .setTitle("Datos Registrados")
                        .setMessage(mensaje)
                        .setPositiveButton("Cerrar", null)
                        .show();

            } catch (Exception e) {
                e.printStackTrace();
                new androidx.appcompat.app.AlertDialog.Builder(RegistroActivity.this)
                        .setTitle("Error")
                        .setMessage("No se encontraron datos registrados.")
                        .setPositiveButton("Cerrar", null)
                        .show();
            }
        });

    }
    public boolean guardarBD (String cedula, String nombre, float rating){
        BDOpenHelper dbAgricultura = new BDOpenHelper(this);
        final SQLiteDatabase dbAgriculturaEdit = dbAgricultura.getWritableDatabase();
        if (dbAgriculturaEdit != null){
            ContentValues cv = new ContentValues();
            cv.put("cedula", cedula);
            cv.put("nombre", nombre);
            cv.put("ratingIngles", rating);

            dbAgriculturaEdit.insert("usuario", null, cv);
            dbAgriculturaEdit.close();
            return true;
        }else{
            dbAgriculturaEdit.close();
            return false;
        }


    }
}

