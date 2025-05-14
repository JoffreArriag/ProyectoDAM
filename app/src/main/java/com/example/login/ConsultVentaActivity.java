package com.example.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConsultVentaActivity extends AppCompatActivity {

    private EditText etBuscarVenta;
    private TextView txtNombre, txtCedula, txtProductos, txtTotal;
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

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(this::onBackButtonClick);

        etBuscarVenta.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                buscarVenta(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    // Este metodo debe ser público para que sea reconocido en el XML
    public void onBackButtonClick(View v) {
        startActivity(new Intent(this, HomeActivity.class));
    }

    // Este metodo debe ser público para que sea reconocido en el XML
    public void onEliminarButtonClick(View v) {
        if (cedulaActual != null) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM ventas WHERE cedula = ?", new Object[]{cedulaActual});
            db.close();
            Toast.makeText(this, "Venta eliminada", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Primero busque una venta", Toast.LENGTH_SHORT).show();
        }
    }

    // Este metodo debe ser público para que sea reconocido en el XML
    public void onEditarButtonClick(View v) {
        if (cedulaActual != null) {
            String nombreActual = txtNombre.getText().toString().replace("Nombre: ", "").trim();
            String productosActuales = txtProductos.getText().toString().replace("Productos: ", "").trim();
            mostrarDialogoEditar(nombreActual, productosActuales);
        } else {
            Toast.makeText(this, "Primero busque una venta", Toast.LENGTH_SHORT).show();
        }
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

    private void mostrarDialogoEditar(String nombreActual, String productosActuales) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Editar Venta");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputNombre = new EditText(this);
        inputNombre.setHint("Nombre del cliente");
        inputNombre.setText(nombreActual);
        layout.addView(inputNombre);

        TextView lblProductos = new TextView(this);
        lblProductos.setText("\nSelecciona los productos:");
        layout.addView(lblProductos);

        LinearLayout checkBoxContainer = new LinearLayout(this);
        checkBoxContainer.setOrientation(LinearLayout.VERTICAL);
        layout.addView(checkBoxContainer);

        TextView txtTotalDinamico = new TextView(this);
        txtTotalDinamico.setText("\nTotal: $0.00");
        layout.addView(txtTotalDinamico);

        java.util.List<CheckBox> checkBoxes = new java.util.ArrayList<>();
        java.util.Map<CheckBox, Double> preciosMap = new java.util.HashMap<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre, precio_caja FROM cultivos", null);

        String[] seleccionados = productosActuales.split(";");
        for (int i = 0; i < seleccionados.length; i++) {
            seleccionados[i] = seleccionados[i].trim();
        }

        while (cursor.moveToNext()) {
            String nombre = cursor.getString(0);
            double precio = cursor.getDouble(1);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(nombre + " - $" + precio);

            for (String sel : seleccionados) {
                if (sel.equalsIgnoreCase(nombre)) {
                    checkBox.setChecked(true);
                    break;
                }
            }

            preciosMap.put(checkBox, precio);
            checkBoxes.add(checkBox);
            checkBoxContainer.addView(checkBox);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                double total = 0;
                for (CheckBox cb : checkBoxes) {
                    if (cb.isChecked()) {
                        total += preciosMap.get(cb);
                    }
                }
                txtTotalDinamico.setText("Total: $" + String.format("%.2f", total));
            });
        }

        cursor.close();
        db.close();

        double totalInicial = 0;
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                totalInicial += preciosMap.get(cb);
            }
        }
        txtTotalDinamico.setText("Total: $" + String.format("%.2f", totalInicial));

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nuevoNombre = inputNombre.getText().toString().trim();
            java.util.List<String> productosSeleccionados = new java.util.ArrayList<>();

            for (CheckBox cb : checkBoxes) {
                if (cb.isChecked()) {
                    String texto = cb.getText().toString();
                    String nombreProducto = texto.split(" - ")[0].trim();
                    productosSeleccionados.add(nombreProducto);
                }
            }

            if (!nuevoNombre.isEmpty() && !productosSeleccionados.isEmpty() && cedulaActual != null) {
                String nuevosProductos = String.join(";", productosSeleccionados);

                double nuevoTotal = 0;
                for (CheckBox cb : checkBoxes) {
                    if (cb.isChecked()) {
                        nuevoTotal += preciosMap.get(cb);
                    }
                }

                SQLiteDatabase dbEdit = dbHelper.getWritableDatabase();
                dbEdit.execSQL("UPDATE ventas SET nombre = ?, productos = ?, total = ? WHERE cedula = ?",
                        new Object[]{nuevoNombre, nuevosProductos, nuevoTotal, cedulaActual});
                dbEdit.close();

                Toast.makeText(this, "Venta actualizada correctamente", Toast.LENGTH_SHORT).show();
                buscarVenta(cedulaActual);
            } else {
                Toast.makeText(this, "Campos inválidos o sin productos seleccionados", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
