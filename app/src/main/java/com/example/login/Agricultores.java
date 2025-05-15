package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Agricultores extends AppCompatActivity {

    private List<Agricultor> listaAgricultores = new ArrayList<>();
    private RecyclerView recyclerView;
    private AgricultoresAdapter adapter;
    private DatabaseReference dbRef;
    private EditText editBuscarAgricultor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricultores);

        dbRef = FirebaseDatabase.getInstance().getReference("agricultores");

        recyclerView = findViewById(R.id.recyclerAgricultores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AgricultoresAdapter(listaAgricultores, new AgricultoresAdapter.OnAgricultorAccionListener() {
            @Override
            public void onEditar(Agricultor agricultor, int position) {
                AgregarAgricultorDialog dialog = new AgregarAgricultorDialog();
                dialog.setAgricultorListener(new AgregarAgricultorDialog.AgricultorListener() {
                    @Override
                    public void onAgricultorAgregado(Agricultor nuevo) { }

                    @Override
                    public void onAgricultorEditado(Agricultor editado, int pos) {
                        String id = agricultor.getId();
                        editado.setId(id);
                        dbRef.child(id).setValue(editado);
                        listaAgricultores.set(pos, editado);
                        adapter.notifyItemChanged(pos);
                        Toast.makeText(Agricultores.this, "Agricultor editado", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setAgricultorEditar(agricultor, position);
                dialog.show(getSupportFragmentManager(), "EditarAgricultor");
            }

            @Override
            public void onEliminar(int position) {
                Agricultor agricultor = listaAgricultores.get(position);
                dbRef.child(agricultor.getId()).removeValue();
                listaAgricultores.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(Agricultores.this, "Agricultor eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        editBuscarAgricultor = findViewById(R.id.editBuscarAgricultor);
        editBuscarAgricultor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarAgricultoresPorNombre(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        cargarAgricultoresDesdeFirebase();
    }

    private void cargarAgricultoresDesdeFirebase() {
        dbRef.get().addOnCompleteListener(task -> {
            listaAgricultores.clear();
            if (task.isSuccessful()) {
                for (DataSnapshot snap : task.getResult().getChildren()) {
                    Agricultor agricultor = snap.getValue(Agricultor.class);
                    if (agricultor != null) {
                        listaAgricultores.add(agricultor);
                    }
                }
                adapter.actualizarLista(listaAgricultores); // actualiza con la lista completa
            } else {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtrarAgricultoresPorNombre(String texto) {
        List<Agricultor> listaFiltrada = new ArrayList<>();
        for (Agricultor ag : listaAgricultores) {
            if (ag.getNombre() != null && ag.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(ag);
            }
        }
        adapter.actualizarLista(listaFiltrada);
    }

    public void irAHome(View v) {
        Intent intent = new Intent(Agricultores.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void agregarAgricultor(View v) {
        AgregarAgricultorDialog dialog = new AgregarAgricultorDialog();
        dialog.setAgricultorListener(new AgregarAgricultorDialog.AgricultorListener() {
            @Override
            public void onAgricultorAgregado(Agricultor agricultor) {
                cargarAgricultoresDesdeFirebase();
                Toast.makeText(Agricultores.this, "Agricultor agregado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAgricultorEditado(Agricultor agricultor, int position) { }
        });
        dialog.show(getSupportFragmentManager(), "AgregarAgricultor");
    }
}
