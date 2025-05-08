package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MercadoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CultivoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercado);

        recyclerView = findViewById(R.id.recyclerProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 🔁 Reemplazo de dbHelper.obtenerTodosLosCultivos():
        List<Cultivo> cultivos = cargarCultivosDesdeBD();

        adapter = new CultivoAdapter(cultivos);
        recyclerView.setAdapter(adapter);

        Button btnVender = findViewById(R.id.btnVender);
        btnVender.setOnClickListener(v -> mostrarDialogoVenta());

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> startActivity(new Intent(MercadoActivity.this, HomeActivity.class)));
    }

    // ✅ Carga directa desde la base de datos sin usar métodos de BDOpenHelper
    private List<Cultivo> cargarCultivosDesdeBD() {
        List<Cultivo> listaCultivos = new ArrayList<>();
        SQLiteDatabase db = new BDOpenHelper(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cultivos", null);

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria"));
                String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio"));
                String ubicacion = cursor.getString(cursor.getColumnIndexOrThrow("ubicacion"));

                Cultivo cultivo = new Cultivo(nombre, categoria, fechaInicio, ubicacion);
                listaCultivos.add(cultivo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listaCultivos;
    }

    private void mostrarDialogoVenta() {
        List<Cultivo> seleccionados = adapter.getCultivosSeleccionados();

        if (seleccionados.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un cultivo", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.layout_dialog_venta, null);
        builder.setView(dialogView);

        TextView listaProductos = dialogView.findViewById(R.id.listaProductos);
        Button btnGenerar = dialogView.findViewById(R.id.btnGenerarVenta);

        StringBuilder nombres = new StringBuilder();
        for (Cultivo c : seleccionados) {
            nombres.append("- ").append(c.getNombre()).append("\n");
        }
        listaProductos.setText(nombres.toString());

        AlertDialog dialog = builder.create();

        btnGenerar.setOnClickListener(v -> {
            generarVenta(seleccionados);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void generarVenta(List<Cultivo> seleccionados) {
        Toast.makeText(this, "Venta generada con " + seleccionados.size() + " productos", Toast.LENGTH_LONG).show();
    }
}
