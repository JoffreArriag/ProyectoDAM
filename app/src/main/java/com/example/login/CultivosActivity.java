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
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

public class CultivosActivity extends AppCompatActivity {

    private final Map<String, List<Cultivo>> cultivosPorCategoria = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivos);

        cargarCultivosDesdeBD();

        ImageButton btnCereales = findViewById(R.id.btnCereales);
        ImageButton btnLeguminosas = findViewById(R.id.btnLeguminosas);
        ImageButton btnIndustriales = findViewById(R.id.btnIndustriales);
        ImageButton btnHortalizas = findViewById(R.id.btnHortalizas);
        ImageButton btnFrutales = findViewById(R.id.btnFrutales);
        ImageView backButton = findViewById(R.id.backButton);
        Button btnAgregarCultivo = findViewById(R.id.btnAgregarCultivo);

        backButton.setOnClickListener(v -> startActivity(new Intent(CultivosActivity.this, HomeActivity.class)));

        btnAgregarCultivo.setOnClickListener(v -> {
            AgregarCultivoDialog dialog = new AgregarCultivoDialog();
            dialog.setCultivoListener(cultivo -> {
                String categoria = cultivo.getCategoria();
                cultivosPorCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(cultivo);
                Toast.makeText(CultivosActivity.this, "Cultivo agregado", Toast.LENGTH_SHORT).show();
            });
            dialog.show(getSupportFragmentManager(), "AgregarCultivo");
        });

        btnCereales.setOnClickListener(v -> mostrarCultivosPorCategoria(v, "Cereales"));
        btnLeguminosas.setOnClickListener(v -> mostrarCultivosPorCategoria(v, "Leguminosas"));
        btnIndustriales.setOnClickListener(v -> mostrarCultivosPorCategoria(v, "Industriales"));
        btnHortalizas.setOnClickListener(v -> mostrarCultivosPorCategoria(v, "Hortalizas"));
        btnFrutales.setOnClickListener(v -> mostrarCultivosPorCategoria(v, "Frutales"));

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

                        SQLiteDatabase db = new BDOpenHelper(this).getReadableDatabase();
                        Cursor cursor = db.rawQuery(
                                "SELECT * FROM cultivos WHERE nombre LIKE ? OR ubicacion LIKE ? LIMIT 1",
                                new String[]{"%" + criterio + "%", "%" + criterio + "%"}
                        );

                        if (cursor.moveToFirst()) {
                            Cultivo cultivo = new Cultivo(
                                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")),
                                    cursor.getDouble(cursor.getColumnIndexOrThrow("precio_caja"))
                            );

                            cursor.close();
                            db.close();

                            String mensaje = "Nombre: " + cultivo.getNombre()
                                    + "\nCategoría: " + cultivo.getCategoria()
                                    + "\nFecha inicio: " + cultivo.getFechaInicio()
                                    + "\nUbicación: " + cultivo.getUbicacion()
                                    + "\nPrecio por caja: $" + String.format("%.2f", cultivo.getPrecioCaja());

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Datos del Cultivo")
                                    .setMessage(mensaje)
                                    .setPositiveButton("Regresar", null)
                                    .show();

                        } else {
                            cursor.close();
                            db.close();

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Sin resultados")
                                    .setMessage("No se encontró ningún cultivo con ese nombre o ubicación.")
                                    .setPositiveButton("Cerrar", null)
                                    .show();
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

    }

    private void cargarCultivosDesdeBD() {
        SQLiteDatabase db = new BDOpenHelper(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cultivos", null);

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));
                String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio"));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"));
                double precioCaja = cursor.getDouble(cursor.getColumnIndexOrThrow("precio_caja"));

                Cultivo cultivo = new Cultivo(nombre, categoria, fechaInicio, ubicacion, precioCaja);
                cultivosPorCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(cultivo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    private void mostrarCultivosPorCategoria(View view, String categoria) {
        List<Cultivo> cultivos = cultivosPorCategoria.get(categoria);
        if (cultivos == null || cultivos.isEmpty()) {
            Toast.makeText(this, "No hay cultivos en " + categoria, Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_mostrar_cultivos, null);
        LinearLayout layoutCultivos = dialogView.findViewById(R.id.layoutCultivos);
        layoutCultivos.removeAllViews();

        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo cultivo = cultivos.get(i);
            View itemView = inflater.inflate(R.layout.item_cultivo, layoutCultivos, false);
            TextView tvInfo = itemView.findViewById(R.id.tvCultivoInfo);
            Button btnEditar = itemView.findViewById(R.id.btnEditar);
            Button btnEliminar = itemView.findViewById(R.id.btnEliminar);

            tvInfo.setText(getString(R.string.cultivo_info, cultivo.getNombre(), cultivo.getFechaInicio(), cultivo.getUbicacion(), cultivo.getPrecioCaja()));
            int index = i;

            btnEliminar.setOnClickListener(v -> {
                cultivos.remove(index);

                SQLiteDatabase db = new BDOpenHelper(this).getWritableDatabase();
                int resultado = db.delete("cultivos", "nombre = ? AND fecha_inicio = ?",
                        new String[]{cultivo.getNombre(), cultivo.getFechaInicio()});
                db.close();

                if (resultado > 0) {
                    Toast.makeText(this, "Cultivo eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al eliminar cultivo", Toast.LENGTH_SHORT).show();
                }

                mostrarCultivosPorCategoria(view, categoria);
            });

            btnEditar.setOnClickListener(v -> {
                AgregarCultivoDialog dialog = new AgregarCultivoDialog();
                dialog.setCultivo(cultivo);

                dialog.setCultivoListener(cultivoEditado -> {
                    SQLiteDatabase db = new BDOpenHelper(this).getWritableDatabase();

                    ContentValues valores = new ContentValues();
                    valores.put("nombre", cultivoEditado.getNombre());
                    valores.put("categoria", cultivoEditado.getCategoria());
                    valores.put("fecha_inicio", cultivoEditado.getFechaInicio());
                    valores.put("ubicacion", cultivoEditado.getUbicacion());
                    valores.put("precio_caja", cultivoEditado.getPrecioCaja());

                    int resultado = db.update("cultivos", valores, "nombre = ? AND categoria = ? AND fecha_inicio = ?",
                            new String[]{cultivo.getNombre(), cultivo.getCategoria(), cultivo.getFechaInicio()});
                    db.close();

                    if (resultado > 0) {
                        cultivosPorCategoria.get(categoria).set(index, cultivoEditado);
                        Toast.makeText(this, "Cultivo editado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error al editar el cultivo", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show(getSupportFragmentManager(), "editarCultivo");
            });

            layoutCultivos.addView(itemView);
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cultivos en " + categoria)
                .setView(dialogView)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
