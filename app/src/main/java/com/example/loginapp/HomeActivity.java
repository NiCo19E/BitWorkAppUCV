package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        searchInput = findViewById(R.id.searchInput);

        // Seleccionamos Home como activo en el BottomNavigation
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Ya estamos en Home, no hacemos nada
                return true;

            } else if (itemId == R.id.nav_ads) {
                startActivity(new Intent(this, AdsActivity.class));
                return true;

            } else if (itemId == R.id.nav_create_ad) {
                startActivity(new Intent(this, CreateAdActivity.class));
                return true;

            } else if (itemId == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                return true;

            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;

            } else {
                return false;
            }
        });
    }
}
