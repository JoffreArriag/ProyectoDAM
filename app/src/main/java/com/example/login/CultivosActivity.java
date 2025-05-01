package com.example.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CultivosActivity extends AppCompatActivity {

    private Map<String, List<Cultivo>> cultivosPorCategoria = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivos);

        // Inicialización de los botones
        ImageButton btnCereales = findViewById(R.id.btnCereales);
        ImageButton btnLeguminosas = findViewById(R.id.btnLeguminosas);
        ImageButton btnIndustriales = findViewById(R.id.btnIndustriales);
        ImageButton btnHortalizas = findViewById(R.id.btnHortalizas);
        ImageButton btnFrutales = findViewById(R.id.btnFrutales);
        ImageView backButton = findViewById(R.id.backButton);
        Button btnAgregarCultivo = findViewById(R.id.btnAgregarCultivo);

        // boton regresar
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(CultivosActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        // Botón Agregar Cultivo
        btnAgregarCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarCultivoDialog dialog = new AgregarCultivoDialog();
                dialog.setCultivoListener(new AgregarCultivoDialog.CultivoListener() {
                    @Override
                    public void onCultivoAgregado(Cultivo cultivo) {
                        String categoria = cultivo.getCategoria();
                        if (!cultivosPorCategoria.containsKey(categoria)) {
                            cultivosPorCategoria.put(categoria, new ArrayList<>());
                        }
                        cultivosPorCategoria.get(categoria).add(cultivo);
                        Toast.makeText(CultivosActivity.this, "Cultivo agregado", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show(getSupportFragmentManager(), "AgregarCultivo");
            }
        });

        // Botones de Categorías
        btnCereales.setOnClickListener(v -> mostrarCultivosPorCategoria("Cereales"));
        btnLeguminosas.setOnClickListener(v -> mostrarCultivosPorCategoria("Leguminosas"));
        btnIndustriales.setOnClickListener(v -> mostrarCultivosPorCategoria("Industriales"));
        btnHortalizas.setOnClickListener(v -> mostrarCultivosPorCategoria("Hortalizas"));
        btnFrutales.setOnClickListener(v -> mostrarCultivosPorCategoria("Frutales"));
    }

    private void mostrarCultivosPorCategoria(String categoria) {
        List<Cultivo> cultivos = cultivosPorCategoria.get(categoria);
        if (cultivos == null || cultivos.isEmpty()) {
            Toast.makeText(this, "No hay cultivos en " + categoria, Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder mensaje = new StringBuilder();
        for (Cultivo c : cultivos) {
            mensaje.append("• ").append(c.getNombre()).append("\n")
                    .append("   Fecha: ").append(c.getFechaInicio()).append("\n")
                    .append("   Ubicación: ").append(c.getUbicacion()).append("\n\n");
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cultivos en " + categoria)
                .setMessage(mensaje.toString())
                .setPositiveButton("Cerrar", null)
                .show();
    }
}
