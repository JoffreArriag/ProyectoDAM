package com.example.login;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agricultores);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Agricultores.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        recyclerView = findViewById(R.id.recyclerAgricultores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AgricultoresAdapter(listaAgricultores);


        recyclerView.setAdapter(adapter);

        // Botón para agregar un nuevo agricultor
        Button btnAgregarAgricultor = findViewById(R.id.btnAgregarAgricultor);
        btnAgregarAgricultor.setOnClickListener(v -> showAgregarAgricultorDialog());
    }

    private void showAgregarAgricultorDialog() {
        AgregarAgricultorDialog dialog = new AgregarAgricultorDialog();
        dialog.setAgricultorListener(new AgregarAgricultorDialog.AgricultorListener() {
            @Override
            public void onAgricultorAgregado(Agricultor agricultor) {
                listaAgricultores.add(agricultor);
                adapter.notifyDataSetChanged();
                Toast.makeText(Agricultores.this, "Agricultor agregado", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getSupportFragmentManager(), "AgregarAgricultor");
    }
}
