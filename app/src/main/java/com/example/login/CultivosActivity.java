package com.example.login;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class CultivosActivity extends AppCompatActivity {

    private ArrayList<String> listaCultivos;
    private ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cultivos);

        // Declaración local de las vistas
        ListView listViewCultivos = findViewById(R.id.listViewCultivos);
        ImageButton btnAgregar = findViewById(R.id.btnAgregar);

        // Lista de cultivos y adaptador
        listaCultivos = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCultivos);
        listViewCultivos.setAdapter(adaptador);

        // Botón para agregar nuevo cultivo
        btnAgregar.setOnClickListener(v -> agregarCultivo());

        // Click para editar o eliminar cultivo
        listViewCultivos.setOnItemClickListener((parent, view, position, id) -> mostrarOpciones(position));
    }

    // Método para agregar cultivo
    private void agregarCultivo() {
        listaCultivos.add("Nuevo Cultivo " + (listaCultivos.size() + 1));
        adaptador.notifyDataSetChanged();
    }

    // Método para mostrar opciones de Editar o Eliminar
    private void mostrarOpciones(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones")
                .setItems(new CharSequence[]{"Editar", "Eliminar"}, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                editarCultivo(position);
                                break;
                            case 1:
                                eliminarCultivo(position);
                                break;
                        }
                });
        builder.show();
    }

    // Método para editar cultivo
    private void editarCultivo(int position) {
        listaCultivos.set(position, listaCultivos.get(position) + " (Editado)");
        adaptador.notifyDataSetChanged();
        Toast.makeText(this, "Cultivo editado", Toast.LENGTH_SHORT).show();
    }

    // Método para eliminar cultivo
    private void eliminarCultivo(int position) {
        listaCultivos.remove(position);
        adaptador.notifyDataSetChanged();
        Toast.makeText(this, "Cultivo eliminado", Toast.LENGTH_SHORT).show();
    }
}
