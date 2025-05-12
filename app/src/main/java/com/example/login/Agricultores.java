package com.example.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Agricultores extends AppCompatActivity {

    private List<Agricultor> listaAgricultores = new ArrayList<>();
    private RecyclerView recyclerView;
    private AgricultoresAdapter adapter;
    private BDOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricultores);

        dbHelper = new BDOpenHelper(this);
        cargarAgricultoresDesdeBD();

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Agricultores.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

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
                        Agricultor original = listaAgricultores.get(pos);
                        editado.setId(original.getId());

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("nombre", editado.getNombre());
                        values.put("edad", editado.getEdad());
                        values.put("zona", editado.getZona());
                        values.put("experiencia", editado.getExperiencia());
                        db.update("agricultores", values, "id = ?", new String[]{String.valueOf(editado.getId())});

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
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("agricultores", "id = ?", new String[]{String.valueOf(agricultor.getId())});
                listaAgricultores.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(Agricultores.this, "Agricultor eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        Button btnAgregarAgricultor = findViewById(R.id.btnAgregarAgricultor);
        btnAgregarAgricultor.setOnClickListener(v -> showAgregarAgricultorDialog());
    }

    private void cargarAgricultoresDesdeBD() {
        listaAgricultores.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM agricultores", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                int edad = cursor.getInt(cursor.getColumnIndexOrThrow("edad"));
                String zona = cursor.getString(cursor.getColumnIndexOrThrow("zona"));
                String experiencia = cursor.getString(cursor.getColumnIndexOrThrow("experiencia"));
                listaAgricultores.add(new Agricultor(id, nombre, edad, zona, experiencia));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void showAgregarAgricultorDialog() {
        AgregarAgricultorDialog dialog = new AgregarAgricultorDialog();
        dialog.setAgricultorListener(new AgregarAgricultorDialog.AgricultorListener() {
            @Override
            public void onAgricultorAgregado(Agricultor agricultor) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("nombre", agricultor.getNombre());
                values.put("edad", agricultor.getEdad());
                values.put("zona", agricultor.getZona());
                values.put("experiencia", agricultor.getExperiencia());
                long id = db.insert("agricultores", null, values);
                agricultor.setId((int) id);

                listaAgricultores.add(agricultor);
                adapter.notifyItemInserted(listaAgricultores.size() - 1);
                Toast.makeText(Agricultores.this, "Agricultor agregado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAgricultorEditado(Agricultor agricultor, int position) { }
        });
        dialog.show(getSupportFragmentManager(), "AgregarAgricultor");
    }
}
