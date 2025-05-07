package com.example.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.LayoutInflater;
import android.widget.EditText;


public class CultivosActivity extends AppCompatActivity {

    private final Map<String, List<Cultivo>> cultivosPorCategoria = new HashMap<>();
    private BDOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivos);

        dbHelper = new BDOpenHelper(this);
        cargarCultivosDesdeBD();

        // Referencias UI
        ImageButton btnCereales = findViewById(R.id.btnCereales);
        ImageButton btnLeguminosas = findViewById(R.id.btnLeguminosas);
        ImageButton btnIndustriales = findViewById(R.id.btnIndustriales);
        ImageButton btnHortalizas = findViewById(R.id.btnHortalizas);
        ImageButton btnFrutales = findViewById(R.id.btnFrutales);
        ImageView backButton = findViewById(R.id.backButton);
        Button btnAgregarCultivo = findViewById(R.id.btnAgregarCultivo);

        // Acciones de los botones
        backButton.setOnClickListener(v -> startActivity(new Intent(CultivosActivity.this, HomeActivity.class)));

        btnAgregarCultivo.setOnClickListener(v -> {
            AgregarCultivoDialog dialog = new AgregarCultivoDialog();
            dialog.setCultivoListener(cultivo -> {
                String categoria = cultivo.getCategoria();
                cultivosPorCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(cultivo);
                dbHelper.insertarCultivo(cultivo);
                Toast.makeText(CultivosActivity.this, "Cultivo agregado", Toast.LENGTH_SHORT).show();
            });
            dialog.show(getSupportFragmentManager(), "AgregarCultivo");
        });

        // Mostrar cultivos por categoría
        btnCereales.setOnClickListener(v -> mostrarCultivosPorCategoria("Cereales"));
        btnLeguminosas.setOnClickListener(v -> mostrarCultivosPorCategoria("Leguminosas"));
        btnIndustriales.setOnClickListener(v -> mostrarCultivosPorCategoria("Industriales"));
        btnHortalizas.setOnClickListener(v -> mostrarCultivosPorCategoria("Hortalizas"));
        btnFrutales.setOnClickListener(v -> mostrarCultivosPorCategoria("Frutales"));

        Button btnBuscarCultivo = findViewById(R.id.btnBuscarCultivo);

        btnBuscarCultivo.setOnClickListener(view -> {
            EditText inputBusqueda = new EditText(CultivosActivity.this);
            inputBusqueda.setHint("Ingrese nombre o ubicación del cultivo");

            new androidx.appcompat.app.AlertDialog.Builder(CultivosActivity.this)
                    .setTitle("Buscar Cultivo")
                    .setView(inputBusqueda)
                    .setPositiveButton("Buscar", (dialog, which) -> {
                        String criterio = inputBusqueda.getText().toString().trim();

                        if (criterio.isEmpty()) {
                            Toast.makeText(this, "Debe ingresar nombre o ubicación", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Cultivo> resultados = dbHelper.buscarCultivosPorNombreOUbicacion(criterio);

                        if (!resultados.isEmpty()) {
                            // Mostrar los resultados en un nuevo AlertDialog
                            LayoutInflater inflater = LayoutInflater.from(this);
                            View viewResultado = inflater.inflate(R.layout.dialog_mostrar_cultivos, null);
                            LinearLayout layoutCultivos = viewResultado.findViewById(R.id.layoutCultivos);
                            layoutCultivos.removeAllViews();

                            for (Cultivo cultivo : resultados) {
                                View itemView = inflater.inflate(R.layout.item_cultivo, layoutCultivos, false);
                                TextView tvInfo = itemView.findViewById(R.id.tvCultivoInfo);
                                tvInfo.setText(getString(R.string.cultivo_info, cultivo.getNombre(), cultivo.getFechaInicio(), cultivo.getUbicacion()));
                                layoutCultivos.addView(itemView);
                            }

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Resultados de búsqueda")
                                    .setView(viewResultado)
                                    .setPositiveButton("Cerrar", null)
                                    .show();

                        } else {
                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Sin resultados")
                                    .setMessage("No se encontraron cultivos con ese nombre o ubicación.")
                                    .setPositiveButton("Cerrar", null)
                                    .show();
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

    }

    // Cargar los cultivos desde la base de datos
    private void cargarCultivosDesdeBD() {
        List<Cultivo> todosCultivos = dbHelper.obtenerTodosLosCultivos();
        for (Cultivo cultivo : todosCultivos) {
            cultivosPorCategoria
                    .computeIfAbsent(cultivo.getCategoria(), k -> new ArrayList<>())
                    .add(cultivo);
        }
    }

    // Mostrar los cultivos por categoría
    private void mostrarCultivosPorCategoria(String categoria) {
        List<Cultivo> cultivos = cultivosPorCategoria.get(categoria);
        if (cultivos == null || cultivos.isEmpty()) {
            Toast.makeText(this, "No hay cultivos en " + categoria, Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_mostrar_cultivos, null);
        LinearLayout layoutCultivos = view.findViewById(R.id.layoutCultivos);
        layoutCultivos.removeAllViews();

        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo cultivo = cultivos.get(i);


            View itemView = inflater.inflate(R.layout.item_cultivo, layoutCultivos, false);
            TextView tvInfo = itemView.findViewById(R.id.tvCultivoInfo);
            Button btnEditar = itemView.findViewById(R.id.btnEditar);
            Button btnEliminar = itemView.findViewById(R.id.btnEliminar);

            tvInfo.setText(getString(R.string.cultivo_info, cultivo.getNombre(), cultivo.getFechaInicio(), cultivo.getUbicacion()));
            int index = i;

            // Acción del botón Eliminar
            btnEliminar.setOnClickListener(v -> {
                cultivos.remove(index);
                dbHelper.eliminarCultivo(cultivo);
                Toast.makeText(this, "Cultivo eliminado", Toast.LENGTH_SHORT).show();
                mostrarCultivosPorCategoria(categoria);
            });

            btnEditar.setOnClickListener(v -> {
                editarCultivo(cultivo, categoria, index);
            });

            layoutCultivos.addView(itemView);
        }


        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cultivos en " + categoria)
                .setView(view)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Editar un cultivo
    private void editarCultivo(Cultivo cultivo, String categoria, int index) {
        AgregarCultivoDialog dialog = new AgregarCultivoDialog();
        dialog.setCultivo(cultivo);

        dialog.setCultivoListener(cultivoEditado -> {
            boolean resultado = dbHelper.actualizarCultivo(cultivo, cultivoEditado);

            if (resultado) {
                cultivosPorCategoria.get(categoria).set(index, cultivoEditado);
                Toast.makeText(this, "Cultivo editado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al editar el cultivo", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show(getSupportFragmentManager(), "editarCultivo");
    }

}
