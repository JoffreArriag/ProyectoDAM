package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    EditText usuario, contraseña;
    CheckBox checkBoxMantenerSesion;
    SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "MisPreferencias";
    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_CONTRASEÑA = "contraseña";
    private static final String KEY_MANTENER_SESION = "mantenerSesion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.usuario);
        contraseña = findViewById(R.id.contraseña);
        checkBoxMantenerSesion = findViewById(R.id.checkBoxMantenerSesion);
        MaterialButton loginbtn = findViewById(R.id.loginbtn);
        TextView olvidemicontraseña = findViewById(R.id.olvideContraseña);
        ImageView googleBtn = findViewById(R.id.googlebtn);
        ImageView facebookBtn = findViewById(R.id.facebookbtn);
        MaterialButton btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Restaurar el estado del checkbox
        boolean mantenerSesion = sharedPreferences.getBoolean(KEY_MANTENER_SESION, false);
        checkBoxMantenerSesion.setChecked(mantenerSesion);

        // Si se guardó usuario y contraseña, hacer autologin
        String savedUsuario = sharedPreferences.getString(KEY_USUARIO, null);
        String savedContraseña = sharedPreferences.getString(KEY_CONTRASEÑA, null);

        if (savedUsuario != null && savedContraseña != null && mantenerSesion) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("nombreUsuario", savedUsuario);
            startActivity(intent);
            finish();
        }

        loginbtn.setOnClickListener(v -> {
            String user = usuario.getText().toString();
            String pass = contraseña.getText().toString();

            if (user.equals("admin") && pass.equals("admin")) {
                Toast.makeText(MainActivity.this, "Acceso Concedido", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (checkBoxMantenerSesion.isChecked()) {
                    editor.putString(KEY_USUARIO, user);
                    editor.putString(KEY_CONTRASEÑA, pass);
                    editor.putBoolean(KEY_MANTENER_SESION, true);
                } else {
                    editor.clear();
                }

                editor.apply();

                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("nombreUsuario", user);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        olvidemicontraseña.setOnClickListener(v -> {
            final EditText inputEmail = new EditText(MainActivity.this);
            inputEmail.setHint("Ingresa tu correo");

            new android.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Recuperar contraseña")
                    .setMessage("Te enviaremos un correo para recuperar tu contraseña")
                    .setView(inputEmail)
                    .setPositiveButton("Enviar", (dialog, which) -> {
                        String correo = inputEmail.getText().toString();
                        if (!correo.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Contraseña enviada a: " + correo, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Debes ingresar un correo", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        googleBtn.setOnClickListener(v -> {
                Toast.makeText(this, "Iniciando sesión con Google...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("nombreUsuario", "Xavier");
                startActivity(intent);
                finish();
                });

        facebookBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando sesión con Facebook...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("nombreUsuario", "Carlos");
            startActivity(intent);
            finish();
        });

        btnCrearCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }
}
