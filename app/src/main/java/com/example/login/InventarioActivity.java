package com.example.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private BDOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        dbHelper = new BDOpenHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerInsumos);
        cargarInsumosDesdeBD();

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
                        InsumoAgricola original = listaInsumos.get(pos);
                        i.setId(original.getId());

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("nombre", i.getNombre());
                        values.put("descripcion", i.getDescripcion());
                        values.put("cantidad", i.getCantidad());
                        db.update("insumos", values, "id = ?", new String[]{String.valueOf(i.getId())});

                        listaInsumos.set(pos, i);
                        adapter.notifyItemChanged(pos);
                        Toast.makeText(InventarioActivity.this, "Insumo editado", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show(getSupportFragmentManager(), "EditarInsumo");
            }

            @Override
            public void onEliminar(int position) {
                InsumoAgricola insumo = listaInsumos.get(position);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("insumos", "id = ?", new String[]{String.valueOf(insumo.getId())});
                listaInsumos.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(InventarioActivity.this, "Insumo eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void cargarInsumosDesdeBD() {
        listaInsumos.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM insumos", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
                listaInsumos.add(new InsumoAgricola(id, nombre, descripcion, cantidad));
            } while (cursor.moveToNext());
        }
        cursor.close();
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
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("nombre", insumo.getNombre());
                values.put("descripcion", insumo.getDescripcion());
                values.put("cantidad", insumo.getCantidad());
                long id = db.insert("insumos", null, values);
                insumo.setId((int) id);

                listaInsumos.add(insumo);
                adapter.notifyItemInserted(listaInsumos.size() - 1);
                Toast.makeText(InventarioActivity.this, "Insumo agregado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInsumoEditado(InsumoAgricola insumo, int position) {}
        });
        dialog.show(getSupportFragmentManager(), "AgregarInsumo");
    }
}
