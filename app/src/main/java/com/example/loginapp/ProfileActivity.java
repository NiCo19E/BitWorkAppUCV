package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewEmail;
    private EditText editTextName, editTextNewPassword;
    private Button btnUpdateName, btnChangePassword, btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        textViewEmail = findViewById(R.id.textViewEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        btnUpdateName = findViewById(R.id.btnUpdateName);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);

        if (currentUser != null) {
            textViewEmail.setText(currentUser.getEmail());
            if (currentUser.getDisplayName() != null) {
                editTextName.setText(currentUser.getDisplayName());
            }
        }

        btnUpdateName.setOnClickListener(v -> {
            String newName = editTextName.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(ProfileActivity.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Nombre actualizado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Error al actualizar nombre", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnChangePassword.setOnClickListener(v -> {
            String newPassword = editTextNewPassword.getText().toString().trim();
            if (newPassword.length() < 6) {
                Toast.makeText(ProfileActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                            editTextNewPassword.setText("");
                        } else {
                            Toast.makeText(ProfileActivity.this, "Error al actualizar contraseña: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(ProfileActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfileActivity.this, AuthActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        });
    }
}
