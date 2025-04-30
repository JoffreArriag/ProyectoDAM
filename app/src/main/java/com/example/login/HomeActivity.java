package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    TextView usernameTextView;
    ImageView ic_info, logoutIcon, deleteIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usernameTextView = findViewById(R.id.usernameTextView);
        ic_info = findViewById(R.id.ic);
        logoutIcon = findViewById(R.id.logoutIcon);
        deleteIcon = findViewById(R.id.deleteIcon);


        String username = getIntent().getStringExtra("nombreUsuario");
        if (username != null) {
            usernameTextView.setText("Bienvenido, " + username);
        }

        ic_info.setOnClickListener(v -> {
            Intent intent = new Intent(this, AcercaDeActivity.class);
            startActivity(intent);
        });

        logoutIcon.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        deleteIcon.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("Mis Preferencias", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(HomeActivity.this, "Datos borrados correctamente", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
