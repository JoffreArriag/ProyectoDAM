package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
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


        usernameTextView = findViewById(R.id.usernameTextView);
        ic_info = findViewById(R.id.ic);
        logoutIcon = findViewById(R.id.logoutIcon);
        deleteIcon = findViewById(R.id.deleteIcon);

        // Inicialización de las tarjetas
        cardCultivos = findViewById(R.id.cardCultivos);
        cardAgricultores = findViewById(R.id.cardAgricultores);
        cardInventario = findViewById(R.id.cardInventario);
        cardRiego = findViewById(R.id.cardRiego);
        cardMercado = findViewById(R.id.cardMercado);

        // Obtener el nombre del usuario
        String username = getIntent().getStringExtra("nombreUsuario");
        if (username != null) {
            usernameTextView.setText("Bienvenido, " + username);
        }

        // Acción para el icono de información
        ic_info.setOnClickListener(v -> {
            Intent intent = new Intent(this, AcercaDeActivity.class);
            startActivity(intent);
        });

        // Acción para el icono de logout
        logoutIcon.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Acción para eliminar preferencias
        deleteIcon.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(HomeActivity.this, "Contenido eliminado", Toast.LENGTH_SHORT).show();
        });

        // Acciones de las tarjetas
        cardCultivos.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CultivosActivity.class);
            startActivity(intent);
        });

        cardAgricultores.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, Agricultores.class);
            startActivity(intent);
        });

        cardInventario.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, InventarioAgricola.class);
            startActivity(intent);
        });

        cardRiego.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TareaRiegoFertilizacion.class);
            startActivity(intent);
        });

        cardMercado.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MercadoVentas.class);
            startActivity(intent);
        });
    }
}
