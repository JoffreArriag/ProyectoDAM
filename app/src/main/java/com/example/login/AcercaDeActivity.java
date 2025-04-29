package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class AcercaDeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        Button btnRegresar = findViewById(R.id.btnRegresar);

        btnRegresar.setOnClickListener(v -> finish());
    }
}
