package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        List<Cultivo> cultivos = cargarCultivosDesdeBD();

        adapter = new CultivoAdapter(cultivos);
        recyclerView.setAdapter(adapter);

        Button btnCrearVenta = findViewById(R.id.btnCrearVenta);
        btnCrearVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoVenta();
            }
        });

        Button btnConsultarVenta = findViewById(R.id.btnConsultarVenta);
        btnConsultarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MercadoActivity.this, ConsultVentaActivity.class));
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MercadoActivity.this, HomeActivity.class));
            }
        });
    }

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
                double precioCaja = cursor.getDouble(cursor.getColumnIndexOrThrow("precio_caja"));

                Cultivo cultivo = new Cultivo(nombre, categoria, fechaInicio, ubicacion, precioCaja);
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
        TextView txtTotalPagar = dialogView.findViewById(R.id.txtTotalPagar);
        Button btnGenerar = dialogView.findViewById(R.id.btnGenerarVenta);

        StringBuilder nombres = new StringBuilder();

        final double[] total = {0.0};


        for (Cultivo c : seleccionados) {
            nombres.append("- ").append(c.getNombre())
                    .append(" ($").append(String.format("%.2f", c.getPrecioCaja())).append(")\n");
            total[0] += c.getPrecioCaja();
        }

        listaProductos.setText(nombres.toString());
        txtTotalPagar.setText("Total a pagar: $" + String.format("%.2f", total[0]));

        AlertDialog dialog = builder.create();

        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etNombre = dialogView.findViewById(R.id.etNombreCliente);
                EditText etCedula = dialogView.findViewById(R.id.etCedulaCliente);

                String nombre = etNombre.getText().toString().trim();
                String cedula = etCedula.getText().toString().trim();

                if (nombre.isEmpty() || cedula.isEmpty()) {
                    Toast.makeText(MercadoActivity.this, "Debe ingresar nombre y cédula", Toast.LENGTH_SHORT).show();
                    return;
                }


                StringBuilder productos = new StringBuilder();
                for (Cultivo c : seleccionados) {
                    productos.append(c.getNombre()).append("; ");
                }


                SQLiteDatabase db = new BDOpenHelper(MercadoActivity.this).getWritableDatabase();
                db.execSQL("INSERT INTO ventas (cedula, nombre, productos, total) VALUES (?, ?, ?, ?)",
                        new Object[]{cedula, nombre, productos.toString(), total[0]});
                db.close();

                Toast.makeText(MercadoActivity.this, "Venta generada con éxito", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
