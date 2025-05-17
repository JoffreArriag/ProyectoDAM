package com.example.login.tarea;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.HomeActivity;
import com.example.login.R;
import com.example.login.agricultor.AgregarAgricultorDialog;
import com.example.login.agricultor.Agricultor;
import com.example.login.agricultor.Agricultores;
import com.example.login.agricultor.AgricultoresAdapter;
import com.example.login.cultivo.Cultivo;
import com.example.login.cultivo.CultivosActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TareaRiegoFertilizacion extends AppCompatActivity {
    private List<Tarea> listaTareas = new ArrayList<>();
    private RecyclerView recyclerView;
    private TareaAdapter adapter;
    private EditText editBuscarTarea;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_riego_fertilizacion);

        ImageView backButton = findViewById(R.id.backButton);

        recyclerView = findViewById(R.id.recyclerTareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ref = FirebaseDatabase.getInstance().getReference("Tareas");

        adapter = new TareaAdapter(listaTareas, new TareaAdapter.OnTareaAccionListener(){
            @Override
            public void onEditar(Tarea tarea, int position) {
                AgregarTareaDialog dialog = new AgregarTareaDialog();
                dialog.setTareaListener(new AgregarTareaDialog.TareaListener() {
                    @Override
                    public void onTareaAgregado(Tarea nuevo) { }

                    @Override
                    public void onTareaEditado(Tarea editado, int pos) {
                        String id = tarea.getId();
                        editado.setId(id);
                        ref.child(id).setValue(editado);
                        listaTareas.set(pos, editado);
                        adapter.notifyItemChanged(pos);
                        Toast.makeText(TareaRiegoFertilizacion.this, "Agricultor editado", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setTareaEditar(tarea, position);
                dialog.show(getSupportFragmentManager(), "EditarAgricultor");
            }

            @Override
            public void onEliminar(int position) {
                Tarea tarea = listaTareas.get(position);
                ref.child(tarea.getId()).removeValue();
                listaTareas.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(TareaRiegoFertilizacion.this, "Agricultor eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        editBuscarTarea = findViewById(R.id.editBuscarTarea);
        editBuscarTarea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarTareasPorNombre(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        cargarTareasDesdeFirebase();


    }

    public void agregarTarea(View v) {
        showAgregarTareaDialog();
    }
    private void cargarTareasDesdeFirebase() {
        ref.get().addOnCompleteListener(task -> {
            listaTareas.clear();
            if (task.isSuccessful()) {
                for (DataSnapshot snap : task.getResult().getChildren()) {
                    Tarea tarea = snap.getValue(Tarea.class);
                    if (tarea != null) {
                        listaTareas.add(tarea);
                    }
                }
                adapter.actualizarLista(listaTareas); // actualiza con la lista completa
            } else {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtrarTareasPorNombre(String texto) {
        if (adapter == null) return; // Evita el crash

        List<Tarea> listaFiltrada = new ArrayList<>();
        for (Tarea ag : listaTareas) {
            if (ag.getNombre() != null && ag.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(ag);
            }
        }
        adapter.actualizarLista(listaFiltrada);
    }

    public void regresar(View v) {
        Intent intent = new Intent(TareaRiegoFertilizacion.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void showAgregarTareaDialog() {
        AgregarTareaDialog dialog = new AgregarTareaDialog();
        dialog.setTareaListener(new AgregarTareaDialog.TareaListener() {
            @Override
            public void onTareaAgregado(Tarea tarea) {
                cargarTareasDesdeFirebase();
                Toast.makeText(TareaRiegoFertilizacion.this, "Agricultor agregado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTareaEditado(Tarea tarea, int position) {

            }
        });
        dialog.show(getSupportFragmentManager(), "AgregarTarea");
    }

}