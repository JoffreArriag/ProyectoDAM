package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {

    TextView usernameTextView;
    ImageView ic_info, logoutIcon, deleteIcon;
    CardView cardCultivos, cardAgricultores, cardInventario, cardRiego, cardMercado;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Referencias UI
        usernameTextView = findViewById(R.id.usernameTextView);
        ic_info = findViewById(R.id.ic);
        logoutIcon = findViewById(R.id.logoutIcon);
        deleteIcon = findViewById(R.id.deleteIcon);

        cardCultivos = findViewById(R.id.cardCultivos);
        cardAgricultores = findViewById(R.id.cardAgricultores);
        cardInventario = findViewById(R.id.cardInventario);
        cardRiego = findViewById(R.id.cardRiego);
        cardMercado = findViewById(R.id.cardMercado);

        // Mostrar nombre del usuario
        String username = getIntent().getStringExtra("nombreUsuario");
        if (username != null) {
            usernameTextView.setText("Bienvenido, " + username);
        }

        // Acción para mostrar el modal de información del equipo
        View.OnClickListener mostrarModalAcercaDe = v -> {
            LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
            View modalView = inflater.inflate(R.layout.activity_acerca_de, null);

            AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                    .setView(modalView)
                    .create();

            Button btnRegresar = modalView.findViewById(R.id.btnRegresar);
            btnRegresar.setOnClickListener(btn -> dialog.dismiss());

            dialog.show();
        };

        ic_info.setOnClickListener(mostrarModalAcercaDe);

        // Acción para eliminar las preferencias
        deleteIcon.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setMessage("¿Estás seguro de que deseas eliminar esta preferencia?")
                    .setCancelable(false)
                    .setPositiveButton("Sí", (dialog, id) -> {
                        SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("usuario");
                        editor.remove("contraseña");
                        editor.remove("mantenerSesion");
                        editor.apply();

                        Toast.makeText(HomeActivity.this, "Preferencias eliminadas", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.dismiss();
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });



        // Acción para logout
        logoutIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Acciones de las tarjetas
        cardCultivos.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CultivosActivity.class));
        });

        cardAgricultores.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, Agricultores.class));
        });

        cardInventario.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, InventarioActivity.class));
        });

        cardRiego.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TareaRiegoFertilizacion.class));
        });

        cardMercado.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, MercadoActivity.class));
        });
    }
}
