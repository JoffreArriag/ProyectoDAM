package com.example.login;

import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.List;

public class MercadoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CultivoAdapter adapter;
    private BDOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercado);


        recyclerView = findViewById(R.id.recyclerProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new BDOpenHelper(this);
        List<Cultivo> cultivos = dbHelper.obtenerTodosLosCultivos();

        adapter = new CultivoAdapter(cultivos);
        recyclerView.setAdapter(adapter);


        Button btnVender = findViewById(R.id.btnVender);
        btnVender.setOnClickListener(v -> mostrarDialogoVenta());
        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> startActivity(new Intent(MercadoActivity.this, HomeActivity.class)));
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
