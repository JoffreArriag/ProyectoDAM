package com.example.login;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import android.view.LayoutInflater;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

public class CultivosActivity extends AppCompatActivity {

    private final Map<String, List<Cultivo>> cultivosPorCategoria = new HashMap<>();
    private DatabaseReference cultivosRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivos);

        cultivosRef = FirebaseDatabase.getInstance().getReference("cultivos");
        cargarCultivosDesdeFirebase();
    }

    private void cargarCultivosDesdeFirebase() {
        cultivosPorCategoria.clear();

        cultivosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Cultivo cultivo = child.getValue(Cultivo.class);
                    if (cultivo != null) {
                        cultivo.setIdFirebase(child.getKey());
                        String categoria = cultivo.getCategoria();
                        cultivosPorCategoria.computeIfAbsent(categoria, k -> new ArrayList<>()).add(cultivo);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(CultivosActivity.this, "Error al cargar cultivos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void irAHome(View v) {
        startActivity(new Intent(CultivosActivity.this, HomeActivity.class));
        finish();
    }

    public void agregarCultivo(View v) {
        AgregarCultivoDialog dialog = new AgregarCultivoDialog();
        dialog.setCultivoListener(cultivo -> {
            String key = cultivosRef.push().getKey();
            if (key != null) {
                cultivo.setIdFirebase(key);
                cultivosRef.child(key).setValue(cultivo);
                cultivosPorCategoria.computeIfAbsent(cultivo.getCategoria(), k -> new ArrayList<>()).add(cultivo);
                Toast.makeText(CultivosActivity.this, "Cultivo agregado", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getSupportFragmentManager(), "AgregarCultivo");
    }


    public void buscarCultivo(View v) {
        EditText inputBusqueda = new EditText(CultivosActivity.this);
        inputBusqueda.setHint("Ingrese nombre o ubicación del cultivo");

        new androidx.appcompat.app.AlertDialog.Builder(CultivosActivity.this)
                .setTitle("Buscar Cultivo")
                .setView(inputBusqueda)
                .setPositiveButton("Buscar", (dialog, which) -> {
                    String criterio = inputBusqueda.getText().toString().trim();
                    if (criterio.isEmpty()) {
                        Toast.makeText(this, "Debe ingresar nombre o ubicación", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    cultivosRef.get().addOnSuccessListener(snapshot -> {
                        Cultivo cultivoEncontrado = null;

                        for (DataSnapshot child : snapshot.getChildren()) {
                            Cultivo c = child.getValue(Cultivo.class);
                            if (c != null) {
                                if (c.getNombre().toLowerCase().contains(criterio.toLowerCase()) ||
                                        c.getUbicacion().toLowerCase().contains(criterio.toLowerCase())) {
                                    cultivoEncontrado = c;
                                    break;
                                }
                            }
                        }

                        if (cultivoEncontrado != null) {
                            String mensaje = "Nombre: " + cultivoEncontrado.getNombre()
                                    + "\nCategoría: " + cultivoEncontrado.getCategoria()
                                    + "\nFecha inicio: " + cultivoEncontrado.getFechaInicio()
                                    + "\nUbicación: " + cultivoEncontrado.getUbicacion()
                                    + "\nPrecio por caja: $" + String.format("%.2f", cultivoEncontrado.getPrecioCaja());

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Datos del Cultivo")
                                    .setMessage(mensaje)
                                    .setPositiveButton("Regresar", null)
                                    .show();
                        } else {
                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Sin resultados")
                                    .setMessage("No se encontró ningún cultivo con ese nombre o ubicación.")
                                    .setPositiveButton("Cerrar", null)
                                    .show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al buscar cultivo", Toast.LENGTH_SHORT).show();
                    });

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void clickCategoria(View v) {
        String categoria = "";

        if (v.getId() == R.id.btnCereales) {
            categoria = "Cereales";
        } else if (v.getId() == R.id.btnLeguminosas) {
            categoria = "Leguminosas";
        } else if (v.getId() == R.id.btnIndustriales) {
            categoria = "Industriales";
        } else if (v.getId() == R.id.btnHortalizas) {
            categoria = "Hortalizas";
        } else if (v.getId() == R.id.btnFrutales) {
            categoria = "Frutales";
        }

        if (!categoria.isEmpty()) {
            mostrarCultivosPorCategoria(v, categoria);
        }
    }

    private void mostrarCultivosPorCategoria(View view, String categoria) {
        List<Cultivo> cultivos = cultivosPorCategoria.get(categoria);
        if (cultivos == null || cultivos.isEmpty()) {
            Toast.makeText(this, "No hay cultivos en " + categoria, Toast.LENGTH_SHORT).show();
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_mostrar_cultivos, null);
        LinearLayout layoutCultivos = dialogView.findViewById(R.id.layoutCultivos);
        layoutCultivos.removeAllViews();

        for (int i = 0; i < cultivos.size(); i++) {
            Cultivo cultivo = cultivos.get(i);
            View itemView = inflater.inflate(R.layout.item_cultivo, layoutCultivos, false);
            TextView tvInfo = itemView.findViewById(R.id.tvCultivoInfo);
            Button btnEditar = itemView.findViewById(R.id.btnEditar);
            Button btnEliminar = itemView.findViewById(R.id.btnEliminar);

            tvInfo.setText(getString(R.string.cultivo_info, cultivo.getNombre(), cultivo.getFechaInicio(), cultivo.getUbicacion(), cultivo.getPrecioCaja()));
            int index = i;

            btnEliminar.setOnClickListener(v1 -> {
                cultivos.remove(index);
                String keyFirebase = cultivo.getIdFirebase();
                if (keyFirebase != null) {
                    cultivosRef.child(keyFirebase).removeValue();
                }
                Toast.makeText(this, "Cultivo eliminado", Toast.LENGTH_SHORT).show();
                mostrarCultivosPorCategoria(view, categoria);
            });

            btnEditar.setOnClickListener(v2 -> {
                AgregarCultivoDialog dialog = new AgregarCultivoDialog();
                dialog.setCultivo(cultivo);
                dialog.setCultivoListener(cultivoEditado -> {
                    String keyFirebase = cultivo.getIdFirebase();
                    if (keyFirebase != null) {
                        cultivoEditado.setIdFirebase(keyFirebase); // conserva la key
                        cultivosRef.child(keyFirebase).setValue(cultivoEditado);
                    }
                    cultivosPorCategoria.get(categoria).set(index, cultivoEditado);
                    Toast.makeText(this, "Cultivo editado", Toast.LENGTH_SHORT).show();
                });
                dialog.show(getSupportFragmentManager(), "editarCultivo");
            });

            layoutCultivos.addView(itemView);
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cultivos en " + categoria)
                .setView(dialogView)
                .setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
