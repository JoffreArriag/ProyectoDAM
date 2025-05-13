package com.example.login;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConsultarUsuario extends AppCompatActivity {
    int identificador;
    private boolean modoEdicion = false;

    // Referencias a los TextViews y EditTexts
    private TextView lblCedula;
    private EditText txtEditCedula;
    private TextView lblNombres;
    private EditText txtEditNombres;
    private TextView lblApellidos;
    private EditText txtEditApellidos;
    private TextView lblEdad;
    private EditText txtEditEdad;
    private TextView lblNacionalidad;
    private EditText txtEditNacionalidad;
    private TextView lblGenero;
    private EditText txtEditGenero;
    private TextView lblEstadoCivil;
    private EditText txtEditEstadoCivil;
    private TextView lblFechaNacimiento;
    private EditText txtEditFechaNacimiento;
    private TextView lblRatingIngles;
    private EditText txtEditRatingIngles;
    private Button btnEditar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarVistas();
    }
    private void inicializarVistas() {

        lblCedula = findViewById(R.id.cons_lblCedula);
        lblNombres = findViewById(R.id.cons_lblNombres);
        lblApellidos = findViewById(R.id.cons_lblApellidos);
        lblEdad = findViewById(R.id.cons_lblEdad);
        lblNacionalidad = findViewById(R.id.cons_lblNacionalidad);
        lblGenero = findViewById(R.id.cons_lblGenero);
        lblEstadoCivil = findViewById(R.id.cons_lblEstadoCivil);
        lblFechaNacimiento = findViewById(R.id.cons_lblFechaNacimiento);
        lblRatingIngles = findViewById(R.id.cons_lblRatingIngles);


        txtEditCedula = findViewById(R.id.cons_txtEditCedula);
        txtEditNombres = findViewById(R.id.cons_txtEditNombres);
        txtEditApellidos = findViewById(R.id.cons_txtEditApellidos);
        txtEditEdad = findViewById(R.id.cons_txtEditEdad);
        txtEditNacionalidad = findViewById(R.id.cons_txtEditNacionalidad);
        txtEditGenero = findViewById(R.id.cons_txtEditGenero);
        txtEditEstadoCivil = findViewById(R.id.cons_txtEditEstadoCivil);
        txtEditFechaNacimiento = findViewById(R.id.cons_txtEditFechaNacimiento);
        txtEditRatingIngles = findViewById(R.id.cons_txtEditRatingIngles);


        btnEditar = findViewById(R.id.btnEditarUsuario);

        modoEdicion = false;
        btnEditar.setText("Editar");
    }

    @SuppressLint("Range")
    public void consultarDatosBD (View v)  {
        BDOpenHelper BDagricultura = new BDOpenHelper(this);
        final SQLiteDatabase BDagriculturaSelect = BDagricultura.getReadableDatabase();

        EditText cedula = findViewById(R.id.cons_txtCedula);
        String cedulas = cedula.getText().toString();

        Cursor data = BDagriculturaSelect.rawQuery("SELECT id, cedula, nombres, "+
                "apellidos, edad, nacionalidad, genero, estado_civil," +
                "fecha_nacimiento, ratingIngles FROM usuario WHERE cedula =" + cedulas , null);

        if(data!=null){
            if(data.getCount()==0){
                Toast.makeText(this, "No hay registros encontrados",
                        Toast.LENGTH_LONG).show();
            }else{
                data.moveToFirst();
                identificador = data.getInt(data.getColumnIndex("id"));
                lblCedula.setText(data.getString(data.getColumnIndex("cedula")).toString());
                lblNombres.setText(data.getString(data.getColumnIndex("nombres")).toString());
                lblApellidos.setText(data.getString(data.getColumnIndex("apellidos")).toString());
                lblEdad.setText(data.getString(data.getColumnIndex("edad")).toString());
                lblNacionalidad.setText(data.getString(data.getColumnIndex("nacionalidad")).toString());
                lblGenero.setText(data.getString(data.getColumnIndex("genero")).toString());
                lblEstadoCivil.setText(data.getString(data.getColumnIndex("estado_civil")).toString());
                lblFechaNacimiento.setText(data.getString(data.getColumnIndex("fecha_nacimiento")).toString());
                lblRatingIngles.setText(String.valueOf(data.getFloat(data.getColumnIndex("ratingIngles"))));
                Toast.makeText(this, "ID: " + identificador,
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    private void editarUsuario(int idUsuario) {
        if (idUsuario == 0) {
            Toast.makeText(this, "No se puede editar, usuario no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        BDOpenHelper BDagricultura = new BDOpenHelper(this);
        SQLiteDatabase BDagriculturaWrite = BDagricultura.getWritableDatabase();

        // Recopilar los datos de los EditTexts
        String nuevaCedula = txtEditCedula.getText().toString();
        String nuevosNombres = txtEditNombres.getText().toString();
        String nuevosApellidos = txtEditApellidos.getText().toString();
        String nuevaEdad = txtEditEdad.getText().toString();
        String nuevaNacionalidad = txtEditNacionalidad.getText().toString();
        String nuevoGenero = txtEditGenero.getText().toString();
        String nuevoEstadoCivil = txtEditEstadoCivil.getText().toString();
        String nuevaFechaNacimiento = txtEditFechaNacimiento.getText().toString();
        float nuevoRatingIngles = 0.0f;
        try {
            nuevoRatingIngles = Float.parseFloat(txtEditRatingIngles.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El rating de inglés debe ser un número válido.", Toast.LENGTH_SHORT).show();
            BDagriculturaWrite.close();
            BDagricultura.close();
            return;
        }

        // Crear un objeto ContentValues con los nuevos datos
        ContentValues values = new ContentValues();
        values.put("cedula", nuevaCedula);
        values.put("nombres", nuevosNombres);
        values.put("apellidos", nuevosApellidos);
        values.put("edad", nuevaEdad);
        values.put("nacionalidad", nuevaNacionalidad);
        values.put("genero", nuevoGenero);
        values.put("estado_civil", nuevoEstadoCivil);
        values.put("fecha_nacimiento", nuevaFechaNacimiento);
        values.put("ratingIngles", nuevoRatingIngles);


        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idUsuario) };


        int rowsAffected = BDagriculturaWrite.update(
                "usuario",
                values,
                selection,
                selectionArgs
        );

        BDagriculturaWrite.close();
        BDagricultura.close();


        if (rowsAffected > 0) {
            Toast.makeText(this, "Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar el usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarUsuario(int idUsuario) {
        if (idUsuario == 0) {
            Toast.makeText(this, "No se puede eliminar, usuario no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        BDOpenHelper BDagricultura = new BDOpenHelper(this);
        SQLiteDatabase BDagriculturaWrite = BDagricultura.getWritableDatabase();


        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(idUsuario) };



        int rowsAffected = BDagriculturaWrite.delete(
                "usuario",
                selection,
                selectionArgs
        );

        BDagriculturaWrite.close();
        BDagricultura.close();


        if (rowsAffected > 0) {
            Toast.makeText(this, "Usuario eliminado correctamente.", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            identificador = 0;
            Button btnEditar = findViewById(R.id.btnEditarUsuario);
            btnEditar.setEnabled(false);
        } else {
            Toast.makeText(this, "Error al eliminar el usuario.", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarUsuario(View v) {
        eliminarUsuario(identificador);
    }

    public void toggleModoEdicion(View v) {
        modoEdicion = !modoEdicion;
        if(modoEdicion){
            btnEditar.setText("Guardar");
            cambiarAEditText(lblCedula, txtEditCedula);
            cambiarAEditText(lblNombres, txtEditNombres);
            cambiarAEditText(lblApellidos, txtEditApellidos);
            cambiarAEditText(lblEdad, txtEditEdad);
            cambiarAEditText(lblNacionalidad, txtEditNacionalidad);
            cambiarAEditText(lblGenero, txtEditGenero);
            cambiarAEditText(lblEstadoCivil, txtEditEstadoCivil);
            cambiarAEditText(lblFechaNacimiento, txtEditFechaNacimiento);
            cambiarAEditText(lblRatingIngles, txtEditRatingIngles);
        }else{
            btnEditar.setText("Editar");
            editarUsuario(identificador);
            cambiarATextView(lblCedula, txtEditCedula);
            cambiarATextView(lblNombres, txtEditNombres);
            cambiarATextView(lblApellidos, txtEditApellidos);
            cambiarATextView(lblEdad, txtEditEdad);
            cambiarATextView(lblNacionalidad, txtEditNacionalidad);
            cambiarATextView(lblGenero, txtEditGenero);
            cambiarATextView(lblEstadoCivil, txtEditEstadoCivil);
            cambiarATextView(lblFechaNacimiento, txtEditFechaNacimiento);
            cambiarATextView(lblRatingIngles, txtEditRatingIngles);
        }
    }

    private void cambiarAEditText(TextView textView, EditText editText) {

        editText.setText(textView.getText());

        editText.setLayoutParams(textView.getLayoutParams());

        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize());

        editText.setPadding(
                textView.getPaddingLeft(),
                textView.getPaddingTop(),
                textView.getPaddingRight(),
                textView.getPaddingBottom()
        );

        editText.setGravity(textView.getGravity());


        textView.setVisibility(View.GONE);
        editText.setVisibility(View.VISIBLE);

        editText.requestFocus();
    }

    private void cambiarATextView(TextView textView, EditText editText) {

        textView.setText(editText.getText().toString());

        editText.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    private void limpiarCampos() {
        lblCedula.setText("");
        lblNombres.setText("");
        lblApellidos.setText("");
        lblEdad.setText("");
        lblNacionalidad.setText("");
        lblGenero.setText("");
        lblEstadoCivil.setText("");
        lblFechaNacimiento.setText("");
        lblRatingIngles.setText("");

        txtEditCedula.setText("");
        txtEditNombres.setText("");
        txtEditApellidos.setText("");
        txtEditEdad.setText("");
        txtEditNacionalidad.setText("");
        txtEditGenero.setText("");
        txtEditEstadoCivil.setText("");
        txtEditFechaNacimiento.setText("");
        txtEditRatingIngles.setText("");

        if (modoEdicion) {
            toggleModoEdicion(null);
        }
    }
}
