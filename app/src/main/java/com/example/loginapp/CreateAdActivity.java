package com.example.loginapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAdActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput;
    private Button publishButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);

        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        publishButton = findViewById(R.id.publishButton);

        db = FirebaseFirestore.getInstance();

        publishButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();

            if (!title.isEmpty() && !description.isEmpty()) {
                saveAdToFirestore(title, description);
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAdToFirestore(String title, String description) {
        Map<String, Object> ad = new HashMap<>();
        ad.put("title", title);
        ad.put("description", description);

        // Colección "ads"
        db.collection("ads")
                .add(ad)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(CreateAdActivity.this, "Anuncio publicado", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra actividad tras publicar
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateAdActivity.this, "Error al publicar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
