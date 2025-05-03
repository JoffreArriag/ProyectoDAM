package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TareaRiegoFertilizacion extends AppCompatActivity {
    private List<Tarea> listaTareas = new ArrayList<>();
    private RecyclerView recyclerView;
    private TareaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarea_riego_fertilizacion);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(TareaRiegoFertilizacion.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
        recyclerView = findViewById(R.id.recyclerTareas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TareaAdapter(listaTareas, new TareaAdapter.OnTareaAccionListener() {
            @Override
            public void onEditar(Tarea tarea, int position) {
                AgregarTareaDialog dialog = new AgregarTareaDialog();
                dialog.setTareaListener(new AgregarTareaDialog.TareaListener() {
                    @Override
                    public void onTareaAgregado(Tarea nuevo) {
                    }

                    @Override
                    public void onTareaEditado(Tarea editado, int pos) {
                        listaTareas.set(pos, editado);
                        adapter.notifyItemChanged(pos);
                        Toast.makeText(TareaRiegoFertilizacion.this, "Tarea editada", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.setTareaEditar(tarea, position);
                dialog.show(getSupportFragmentManager(), "EditarAgricultor");
            }

            @Override
            public void onEliminar(int position) {
                listaTareas.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(TareaRiegoFertilizacion.this, "Tarea eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        Button btnAgregarTarea = findViewById(R.id.btnAgregarTarea);
        btnAgregarTarea.setOnClickListener(v -> showAgregarTareaDialog());
    }

    private void showAgregarTareaDialog() {
        AgregarTareaDialog dialog = new AgregarTareaDialog();
        dialog.setTareaListener(new AgregarTareaDialog.TareaListener() {
            @Override
            public void onTareaAgregado(Tarea tarea) {
                listaTareas.add(tarea);
                adapter.notifyDataSetChanged();
                Toast.makeText(TareaRiegoFertilizacion.this, "Agricultor agregado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTareaEditado(Tarea tarea, int position) {

            }
        });
        dialog.show(getSupportFragmentManager(), "AgregarTarea");
    }

}