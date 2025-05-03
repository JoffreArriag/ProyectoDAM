package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class InventarioActivity extends AppCompatActivity {

    private List<InsumoAgricola> listaInsumos = new ArrayList<>();
    private InsumoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        RecyclerView recyclerView = findViewById(R.id.recyclerInsumos);
        Button btnAgregar = findViewById(R.id.btnAgregarInsumo);
        ImageView backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(InventarioActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        adapter = new InsumoAdapter(listaInsumos, new InsumoAdapter.OnInsumoAccionListener() {
            @Override
            public void onEditar(InsumoAgricola insumo, int position) {
                AgregarInsumoDialog dialog = new AgregarInsumoDialog();
                dialog.setInsumoEditar(insumo, position);
                dialog.setInsumoListener(new AgregarInsumoDialog.InsumoListener() {
                    @Override
                    public void onInsumoAgregado(InsumoAgricola i) {}

                    @Override
                    public void onInsumoEditado(InsumoAgricola i, int pos) {
                        listaInsumos.set(pos, i);
                        adapter.notifyItemChanged(pos);
                    }
                });
                dialog.show(getSupportFragmentManager(), "EditarInsumo");
            }

            @Override
            public void onEliminar(int position) {
                listaInsumos.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            AgregarInsumoDialog dialog = new AgregarInsumoDialog();
            dialog.setInsumoListener(new AgregarInsumoDialog.InsumoListener() {
                @Override
                public void onInsumoAgregado(InsumoAgricola insumo) {
                    listaInsumos.add(insumo);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onInsumoEditado(InsumoAgricola insumo, int position) {}
            });
            dialog.show(getSupportFragmentManager(), "AgregarInsumo");
        });
    }
}
