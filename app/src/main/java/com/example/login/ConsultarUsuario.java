package com.example.login;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConsultarUsuario extends AppCompatActivity {

    private String identificadorFirebase;
    private boolean modoEdicion = false;

    private TextView lblCedula, lblNombres, lblApellidos, lblEdad, lblNacionalidad, lblGenero, lblEstadoCivil, lblFechaNacimiento, lblRatingIngles;
    private EditText txtEditCedula, txtEditNombres, txtEditApellidos, txtEditEdad, txtEditNacionalidad, txtEditGenero, txtEditEstadoCivil, txtEditFechaNacimiento, txtEditRatingIngles;
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

    public void consultarDatosBD(View v) {
        EditText cedula = findViewById(R.id.cons_txtCedula);
        String cedulas = cedula.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios");
        ref.orderByChild("cedula").equalTo(cedulas)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                identificadorFirebase = userSnapshot.getKey();
                                Usuario usuario = userSnapshot.getValue(Usuario.class);

                                if (usuario != null) {
                                    lblCedula.setText(usuario.getCedula());
                                    lblNombres.setText(usuario.getNombres());
                                    lblApellidos.setText(usuario.getApellidos());
                                    lblEdad.setText(usuario.getEdad());
                                    lblNacionalidad.setText(usuario.getNacionalidad());
                                    lblGenero.setText(usuario.getGenero());
                                    lblEstadoCivil.setText(usuario.getEstadoCivil());
                                    lblFechaNacimiento.setText(usuario.getFechaNacimiento());
                                    lblRatingIngles.setText(String.valueOf(usuario.getRatingIngles()));
                                    Toast.makeText(ConsultarUsuario.this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(ConsultarUsuario.this, "No hay registros encontrados", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ConsultarUsuario.this, "Error de Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editarUsuario(String idUsuario) {
        if (idUsuario == null) {
            Toast.makeText(this, "No se puede editar, usuario no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        float rating = 0;
        try {
            rating = Float.parseFloat(txtEditRatingIngles.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El rating debe ser numérico", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuarioActualizado = new Usuario(
                txtEditCedula.getText().toString(),
                txtEditNombres.getText().toString(),
                txtEditApellidos.getText().toString(),
                txtEditEdad.getText().toString(),
                txtEditNacionalidad.getText().toString(),
                txtEditGenero.getText().toString(),
                txtEditEstadoCivil.getText().toString(),
                txtEditFechaNacimiento.getText().toString(),
                rating
        );

        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(idUsuario)
                .setValue(usuarioActualizado)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void eliminarUsuario(String idUsuario) {
        if (idUsuario == null) {
            Toast.makeText(this, "No se puede eliminar, usuario no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase.getInstance().getReference("usuarios")
                .child(idUsuario)
                .removeValue()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                    identificadorFirebase = null;
                    btnEditar.setEnabled(false);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void eliminarUsuario(View v) {
        eliminarUsuario(identificadorFirebase);
    }

    public void toggleModoEdicion(View v) {
        modoEdicion = !modoEdicion;
        if (modoEdicion) {
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
        } else {
            btnEditar.setText("Editar");
            editarUsuario(identificadorFirebase);
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
        editText.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
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
