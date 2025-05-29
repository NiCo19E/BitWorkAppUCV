package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redirigir directamente al LoginActivity
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
        finish(); // Cerrar MainActivity para que no regrese con el botón atrás
    }
}