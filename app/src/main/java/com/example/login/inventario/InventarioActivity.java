package com.example.login.inventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.login.HomeActivity;
import com.example.login.R;
import com.google.firebase.database.*;
import java.util.*;

public class InventarioActivity extends AppCompatActivity {

    private final List<InsumoAgricola> listaInsumos = new ArrayList<>();
    private final List<InsumoAgricola> listaFiltrada = new ArrayList<>();
    private InsumoAdapter adapter;
    private DatabaseReference databaseRef;
    private EditText editBuscarInsumo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.login.R.layout.activity_inventario);

        databaseRef = FirebaseDatabase.getInstance().getReference("insumos");

        RecyclerView recyclerView = findViewById(com.example.login.R.id.recyclerInsumos);
        editBuscarInsumo = findViewById(R.id.editBuscarInsumo);

        adapter = new InsumoAdapter(listaFiltrada, new InsumoAdapter.OnInsumoAccionListener() {
            @Override
            public void onEditar(InsumoAgricola insumo, int position) {
                AgregarInsumoDialog dialog = new AgregarInsumoDialog();
                dialog.setInsumoEditar(insumo, position);
                dialog.setInsumoListener(new AgregarInsumoDialog.InsumoListener() {
                    @Override
                    public void onInsumoAgregado(InsumoAgricola i) {}

                    @Override
                    public void onInsumoEditado(InsumoAgricola i, int pos) {
                        String key = String.valueOf(i.getId());
                        databaseRef.child(key).setValue(i);
                        listaInsumos.set(pos, i);
                        filtrarInsumos(editBuscarInsumo.getText().toString());
                        Toast.makeText(InventarioActivity.this, "Insumo editado", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show(getSupportFragmentManager(), "EditarInsumo");
            }

            @Override
            public void onEliminar(int position) {
                InsumoAgricola insumo = listaFiltrada.get(position);
                String key = String.valueOf(insumo.getId());
                databaseRef.child(key).removeValue();
                listaInsumos.remove(insumo);
                listaFiltrada.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(InventarioActivity.this, "Insumo eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        cargarInsumosDesdeFirebase();

        editBuscarInsumo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarInsumos(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void cargarInsumosDesdeFirebase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listaInsumos.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    InsumoAgricola insumo = data.getValue(InsumoAgricola.class);
                    listaInsumos.add(insumo);
                }
                filtrarInsumos(editBuscarInsumo.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(InventarioActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtrarInsumos(String texto) {
        listaFiltrada.clear();
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaInsumos);
        } else {
            texto = texto.toLowerCase();
            for (InsumoAgricola insumo : listaInsumos) {
                if (insumo.getNombre().toLowerCase().contains(texto)) {
                    listaFiltrada.add(insumo);
                }
            }
        }
        adapter.actualizarLista(new ArrayList<>(listaFiltrada));
    }

    public void volverAInicio(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    public void agregarInsumo(View v) {
        AgregarInsumoDialog dialog = new AgregarInsumoDialog();
        dialog.setInsumoListener(new AgregarInsumoDialog.InsumoListener() {
            @Override
            public void onInsumoAgregado(InsumoAgricola insumo) {
                int nuevoId = (int) (System.currentTimeMillis() / 1000);
                insumo.setId(nuevoId);
                databaseRef.child(String.valueOf(nuevoId)).setValue(insumo);

                listaInsumos.add(insumo);
                filtrarInsumos(editBuscarInsumo.getText().toString());

                Toast.makeText(InventarioActivity.this, "Insumo agregado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInsumoEditado(InsumoAgricola insumo, int position) {}
        });
        dialog.show(getSupportFragmentManager(), "AgregarInsumo");
    }
}
