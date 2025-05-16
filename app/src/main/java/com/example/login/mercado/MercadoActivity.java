package com.example.login.mercado;

import android.content.Intent;
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

import com.example.login.HomeActivity;
import com.example.login.R;
import com.example.login.cultivo.Cultivo;
import com.example.login.cultivo.CultivoAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        // Cargar cultivos desde Firebase
        cargarCultivosDesdeFirebase();
    }

    private void cargarCultivosDesdeFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cultivos");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Cultivo> listaCultivos = new ArrayList<>();
                for (DataSnapshot cultivoSnapshot : snapshot.getChildren()) {
                    Cultivo cultivo = cultivoSnapshot.getValue(Cultivo.class);
                    if (cultivo != null) {
                        listaCultivos.add(cultivo);
                    }
                }
                adapter = new CultivoAdapter(listaCultivos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MercadoActivity.this, "Error al cargar cultivos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void mostrarDialogoVenta(View v) {
        if (adapter == null) {
            Toast.makeText(this, "Los cultivos aún no están cargados", Toast.LENGTH_SHORT).show();
            return;
        }

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

    public void generarVenta(View v) {
        if (currentDialogView == null) return;

        EditText etNombre = currentDialogView.findViewById(R.id.etNombreCliente);
        EditText etCedula = currentDialogView.findViewById(R.id.etCedulaCliente);

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

        // Guardar en Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ventas");
        Venta venta = new Venta(nombre, productos.toString(), total);
        ref.child(cedula).setValue(venta)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Venta generada con éxito", Toast.LENGTH_LONG).show();
                    if (currentDialog != null) currentDialog.dismiss();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar venta", Toast.LENGTH_SHORT).show());
    }

    public void consultarVenta(View view) {
        startActivity(new Intent(this, ConsultVentaActivity.class));
    }

    public void irAHome(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
