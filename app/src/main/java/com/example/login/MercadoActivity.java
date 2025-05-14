package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

    private View currentDialogView;
    private AlertDialog currentDialog;
    private List<Cultivo> cultivosSeleccionados;
    private double totalVenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercado);

        recyclerView = findViewById(R.id.recyclerProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Cultivo> cultivos = cargarCultivosDesdeBD();
        adapter = new CultivoAdapter(cultivos);
        recyclerView.setAdapter(adapter);
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

    // Metodo para mostrar el diálogo de venta
    public void mostrarDialogoVenta(View v) {
        cultivosSeleccionados = adapter.getCultivosSeleccionados();

        if (cultivosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un cultivo", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        currentDialogView = getLayoutInflater().inflate(R.layout.layout_dialog_venta, null);
        builder.setView(currentDialogView);

        TextView listaProductos = currentDialogView.findViewById(R.id.listaProductos);
        TextView txtTotalPagar = currentDialogView.findViewById(R.id.txtTotalPagar);

        StringBuilder nombres = new StringBuilder();
        totalVenta = 0.0;

        for (Cultivo c : cultivosSeleccionados) {
            nombres.append("- ").append(c.getNombre())
                    .append(" ($").append(String.format("%.2f", c.getPrecioCaja())).append(")\n");
            totalVenta += c.getPrecioCaja();
        }

        listaProductos.setText(nombres.toString());
        txtTotalPagar.setText("Total a pagar: $" + String.format("%.2f", totalVenta));

        currentDialog = builder.create();
        currentDialog.show();
    }

    // Metodo llamado desde el botón en el layout del diálogo
    public void generarVenta(View v) {
        View dialogView = currentDialogView;

        EditText etNombre = dialogView.findViewById(R.id.etNombreCliente);
        EditText etCedula = dialogView.findViewById(R.id.etCedulaCliente);

        String nombre = etNombre.getText().toString().trim();
        String cedula = etCedula.getText().toString().trim();

        if (nombre.isEmpty() || cedula.isEmpty()) {
            Toast.makeText(this, "Debe ingresar nombre y cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Cultivo> seleccionados = adapter.getCultivosSeleccionados();
        if (seleccionados.isEmpty()) {
            Toast.makeText(this, "No hay cultivos seleccionados", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = 0.0;
        StringBuilder productos = new StringBuilder();
        for (Cultivo c : seleccionados) {
            productos.append(c.getNombre()).append("; ");
            total += c.getPrecioCaja();
        }

        SQLiteDatabase db = new BDOpenHelper(this).getWritableDatabase();
        db.execSQL("INSERT INTO ventas (cedula, nombre, productos, total) VALUES (?, ?, ?, ?)",
                new Object[]{cedula, nombre, productos.toString(), total});
        db.close();

        Toast.makeText(this, "Venta generada con éxito", Toast.LENGTH_LONG).show();
        currentDialog.dismiss(); // <- más seguro
    }


    public void consultarVenta(View view) {
        startActivity(new Intent(this, ConsultVentaActivity.class));
    }

    public void irAHome(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
