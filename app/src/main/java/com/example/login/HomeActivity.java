package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
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

    AlertDialog modalAcercaDe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usernameTextView = findViewById(R.id.usernameTextView);
        ic_info = findViewById(R.id.ic);
        logoutIcon = findViewById(R.id.logoutIcon);
        deleteIcon = findViewById(R.id.deleteIcon);

        cardCultivos = findViewById(R.id.cardCultivos);
        cardAgricultores = findViewById(R.id.cardAgricultores);
        cardInventario = findViewById(R.id.cardInventario);
        cardRiego = findViewById(R.id.cardRiego);
        cardMercado = findViewById(R.id.cardMercado);

        String username = getIntent().getStringExtra("nombreUsuario");
        if (username != null) {
            usernameTextView.setText("Bienvenido, " + username);
        }
    }

    public void abrirAcercaDe(View v) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View modalView = inflater.inflate(R.layout.activity_acerca_de, null);

        modalAcercaDe = new AlertDialog.Builder(this)
                .setView(modalView)
                .create();

        Button btnRegresar = modalView.findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(view -> modalAcercaDe.dismiss());

        modalAcercaDe.show();
    }

    public void cerrarSesion(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void borrarPreferencias(View v) {
        new AlertDialog.Builder(this)
                .setMessage("¿Estás seguro de que deseas eliminar esta preferencia?")
                .setCancelable(false)
                .setPositiveButton("Sí", (dialog, id) -> {
                    SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("usuario");
                    editor.remove("contraseña");
                    editor.remove("mantenerSesion");
                    editor.apply();
                    Toast.makeText(this, "Preferencias eliminadas", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                .show();
    }

    public void irAgricultores(View v) {
        startActivity(new Intent(this, Agricultores.class));
    }

    public void irCultivos(View v) {
        startActivity(new Intent(this, CultivosActivity.class));
    }

    public void irInventario(View v) {
        startActivity(new Intent(this, InventarioActivity.class));
    }

    public void irRiego(View v) {
        startActivity(new Intent(this, TareaRiegoFertilizacion.class));
    }

    public void irMercado(View v) {
        startActivity(new Intent(this, MercadoActivity.class));
    }
}
