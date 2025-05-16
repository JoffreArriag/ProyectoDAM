package com.example.login.mercado;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.login.HomeActivity;
import com.example.login.R;
import com.example.login.cultivo.Cultivo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConsultVentaActivity extends AppCompatActivity {

    private EditText etBuscarVenta;
    private TextView txtNombre, txtCedula, txtProductos, txtTotal;
    private String cedulaActual;

    private Button btnEliminar, btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultarventa);

        etBuscarVenta = findViewById(R.id.etBuscarVenta);
        txtNombre = findViewById(R.id.txtNombreCliente);
        txtCedula = findViewById(R.id.txtCedulaCliente);
        txtProductos = findViewById(R.id.txtProductosSeleccionados);
        txtTotal = findViewById(R.id.txtTotalVenta);

        btnEliminar = findViewById(R.id.btnEliminar);
        btnEditar = findViewById(R.id.btnEditar);

        btnEliminar.setEnabled(false);
        btnEditar.setEnabled(false);

        etBuscarVenta.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                buscarVenta(s.toString().trim());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public void onBackButtonClick(View v) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void onEliminarButtonClick(View v) {
        if (cedulaActual != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ventas").child(cedulaActual);
            ref.removeValue()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Venta eliminada", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar venta", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Primero busque una venta", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEditarButtonClick(View v) {
        if (cedulaActual != null) {
            mostrarDialogoEditarVenta();
        } else {
            Toast.makeText(this, "Primero busque una venta", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarVenta(String cedula) {
        if (cedula.isEmpty()) {
            limpiarCampos();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ventas").child(cedula);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cedulaActual = cedula;
                    Venta venta = snapshot.getValue(Venta.class);

                    txtCedula.setText("Cédula: " + cedula);
                    txtNombre.setText("Nombre: " + venta.getNombre());
                    txtProductos.setText("Productos: " + venta.getProductos());
                    txtTotal.setText("Total a pagar: $" + String.format("%.2f", venta.getTotal()));

                    btnEliminar.setEnabled(true);
                    btnEditar.setEnabled(true);
                } else {
                    Toast.makeText(ConsultVentaActivity.this, "No se encontró la venta", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ConsultVentaActivity.this, "Error al buscar venta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void limpiarCampos() {
        txtCedula.setText("Cédula:");
        txtNombre.setText("Nombre:");
        txtProductos.setText("Productos:");
        txtTotal.setText("Total a pagar:");
        cedulaActual = null;

        btnEliminar.setEnabled(false);
        btnEditar.setEnabled(false);
    }

    private void mostrarDialogoEditarVenta() {
        if (cedulaActual == null) return;

        String nombreActual = txtNombre.getText().toString().replace("Nombre: ", "");
        String productosActual = txtProductos.getText().toString().replace("Productos: ", "");
        List<String> productosSeleccionados = new ArrayList<>();
        for (String producto : productosActual.split(",")) {
            productosSeleccionados.add(producto.trim().toLowerCase());
        }


        View dialogView = getLayoutInflater().inflate(R.layout.layout_editar_venta, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombreEditar);
        EditText etCedula = dialogView.findViewById(R.id.etCedulaEditar);
        TextView txtTotal = dialogView.findViewById(R.id.txtTotalEditar);
        LinearLayout layoutCheckboxCultivos = dialogView.findViewById(R.id.layoutCheckboxCultivos);
        Button btnGuardarCambios = dialogView.findViewById(R.id.btnGuardarCambios);

        etNombre.setText(nombreActual);
        etCedula.setText(cedulaActual);
        txtTotal.setText("Total: $0.00");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Editar Venta")
                .setView(dialogView)
                .create();

        DatabaseReference cultivosRef = FirebaseDatabase.getInstance().getReference("cultivos");

        cultivosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Cultivo> listaCultivos = new ArrayList<>();
            final double[] totalVenta = {0.0};

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot cultivoSnap : snapshot.getChildren()) {
                    Cultivo cultivo = cultivoSnap.getValue(Cultivo.class);
                    if (cultivo == null) continue;
                    cultivo.setIdFirebase(cultivoSnap.getKey());
                    listaCultivos.add(cultivo);

                    CheckBox checkBox = new CheckBox(ConsultVentaActivity.this);
                    String label = cultivo.getNombre() + " ($" + cultivo.getPrecioCaja() + ")";
                    checkBox.setText(label);

                    for (String producto : productosSeleccionados) {
                        if (producto.equalsIgnoreCase(cultivo.getNombre().trim())) {
                            checkBox.setChecked(true);
                            totalVenta[0] += cultivo.getPrecioCaja();
                            break;
                        }
                    }


                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            totalVenta[0] += cultivo.getPrecioCaja();
                        } else {
                            totalVenta[0] -= cultivo.getPrecioCaja();
                        }
                        txtTotal.setText("Total: $" + String.format("%.2f", totalVenta[0]));
                    });

                    layoutCheckboxCultivos.addView(checkBox);
                }

                txtTotal.setText("Total: $" + String.format("%.2f", totalVenta[0]));

                btnGuardarCambios.setOnClickListener(v -> {
                    String nuevoNombre = etNombre.getText().toString().trim();
                    String nuevosProductos = "";
                    double nuevoTotal = 0.0;

                    for (int i = 0; i < layoutCheckboxCultivos.getChildCount(); i++) {
                        View child = layoutCheckboxCultivos.getChildAt(i);
                        if (child instanceof CheckBox) {
                            CheckBox cb = (CheckBox) child;
                            if (cb.isChecked()) {
                                String texto = cb.getText().toString();
                                String nombreCultivo = texto.split(" \\(")[0];
                                nuevosProductos += nombreCultivo + ", ";

                                for (Cultivo c : listaCultivos) {
                                    if (c.getNombre().equals(nombreCultivo)) {
                                        nuevoTotal += c.getPrecioCaja();
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (!nuevosProductos.isEmpty()) {
                        nuevosProductos = nuevosProductos.substring(0, nuevosProductos.length() - 2);
                    }

                    if (nuevoNombre.isEmpty() || nuevosProductos.isEmpty()) {
                        Toast.makeText(ConsultVentaActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Venta ventaEditada = new Venta(nuevoNombre, nuevosProductos, nuevoTotal);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ventas").child(cedulaActual);
                    ref.setValue(ventaEditada)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(ConsultVentaActivity.this, "Venta actualizada", Toast.LENGTH_SHORT).show();
                                buscarVenta(cedulaActual);
                                dialog.dismiss();
                            })
                            .addOnFailureListener(e -> Toast.makeText(ConsultVentaActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show());
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ConsultVentaActivity.this, "Error al cargar cultivos", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
