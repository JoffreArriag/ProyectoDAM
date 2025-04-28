package com.example.login;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import com.google.android.material.button.MaterialButton;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView usuario = findViewById(R.id.usuario);
        TextView contraseña = findViewById(R.id.contraseña);

        MaterialButton loginbtn = findViewById(R.id.loginbtn);

        //usuario: admin y contraseña: admin

        loginbtn.setOnClickListener(v -> {
            if (usuario.getText().toString().equals("admin") && contraseña.getText().toString().equals("admin")) {
                Toast.makeText(MainActivity.this, "Acceso Concedido", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("nombreUsuario", usuario.getText().toString());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        //Olvide mi contraseña
        TextView olvidemicontraseña = findViewById(R.id.olvidémicontraseña);
        olvidemicontraseña.setOnClickListener(v -> {
            // Crear un EditText para escribir el email
            final EditText inputEmail = new EditText(MainActivity.this);
            inputEmail.setHint("Ingresa tu correo");

            // Crear el AlertDialog
            new android.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Recuperar contraseña")
                    .setMessage("Te enviaremos un correo para recuperar tu contraseña")
                    .setView(inputEmail)
                    .setPositiveButton("Enviar", (dialog, which) -> {
                        String correo = inputEmail.getText().toString();
                        if (!correo.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Contaseña enviada a: " + correo, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Debes ingresar un correo", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        //Botones de redes sociales
        ImageView googleBtn = findViewById(R.id.googlebtn);
        ImageView facebookBtn = findViewById(R.id.facebookbtn);

        googleBtn.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Iniciando sesión con Google...", Toast.LENGTH_SHORT).show());
        facebookBtn.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Iniciando sesión con Facebook...", Toast.LENGTH_SHORT).show());

        //Boton crear cuenta
        MaterialButton btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        btnCrearCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

    }
}
