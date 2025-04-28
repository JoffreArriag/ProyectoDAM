package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {

    TextView usernameTextView;
    ImageView notificationIcon, logoutIcon;
    CardView cardCultivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        usernameTextView = findViewById(R.id.usernameTextView);
        notificationIcon = findViewById(R.id.notificationIcon);
        logoutIcon = findViewById(R.id.logoutIcon);
        cardCultivos = findViewById(R.id.cardCultivos);

        // Obtener nombre de usuario enviado desde MainActivity
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            usernameTextView.setText(getString(R.string.welcome_message, username));
        }

        // Interacción con campana de notificaciones
        notificationIcon.setOnClickListener(v -> Toast.makeText(this, R.string.no_notifications, Toast.LENGTH_SHORT).show());

        // Interacción con botón de salir
        logoutIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        //Interacción con Card de Cultivos
        cardCultivos.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CultivosActivity.class)));
    }
}

