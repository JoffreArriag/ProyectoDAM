package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConsultVentaActivity extends AppCompatActivity {

    private EditText etBuscarVenta;
    private TextView txtNombre, txtCedula, txtProductos, txtTotal;
    private Button btnEliminar, btnEditar;
    private BDOpenHelper dbHelper;
    private String cedulaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultarventa);

        dbHelper = new BDOpenHelper(this);

        etBuscarVenta = findViewById(R.id.etBuscarVenta);
        txtNombre = findViewById(R.id.txtNombreCliente);
        txtCedula = findViewById(R.id.txtCedulaCliente);
        txtProductos = findViewById(R.id.txtProductosSeleccionados);
        txtTotal = findViewById(R.id.txtTotalVenta);

        btnEliminar = findViewById(R.id.btnEliminar);
        btnEditar = findViewById(R.id.btnEditar);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));

        etBuscarVenta.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                buscarVenta(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        btnEliminar.setOnClickListener(v -> {
            if (cedulaActual != null) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM ventas WHERE cedula = ?", new Object[]{cedulaActual});
                db.close();
                limpiarCampos();
            }
        });

        btnEditar.setOnClickListener(v -> {
            Toast.makeText(this, "Función de editar en desarrollo", Toast.LENGTH_SHORT).show();
        });
    }

    private void buscarVenta(String cedula) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre, productos FROM ventas WHERE cedula = ?", new String[]{cedula});

        if (cursor.moveToFirst()) {
            cedulaActual = cedula;
            String nombre = cursor.getString(0);
            String productosTexto = cursor.getString(1);

            txtCedula.setText("Cédula: " + cedula);
            txtNombre.setText("Nombre: " + nombre);
            txtProductos.setText("Productos: " + productosTexto);

            double total = calcularTotal(productosTexto);
            txtTotal.setText("Total a pagar: $" + String.format("%.2f", total));
        } else {
            limpiarCampos();
        }

        cursor.close();
        db.close();
    }

    private double calcularTotal(String productosTexto) {
        double total = 0.0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] productos = productosTexto.split(";");
        for (String nombreProd : productos) {
            nombreProd = nombreProd.trim();
            if (!nombreProd.isEmpty()) {
                Cursor c = db.rawQuery("SELECT precio_caja FROM cultivos WHERE nombre = ?", new String[]{nombreProd});
                if (c.moveToFirst()) {
                    total += c.getDouble(0);
                }
                c.close();
            }
        }

        db.close();
        return total;
    }

    private void limpiarCampos() {
        txtCedula.setText("Cédula:");
        txtNombre.setText("Nombre:");
        txtProductos.setText("Productos:");
        txtTotal.setText("Total a pagar:");
        cedulaActual = null;
    }
}
