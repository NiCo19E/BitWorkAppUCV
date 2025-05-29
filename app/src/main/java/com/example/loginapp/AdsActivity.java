package com.example.loginapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdsActivity extends AppCompatActivity {

    private ListView listViewAds;
    private ArrayList<String> adsList;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        listViewAds = findViewById(R.id.listViewAds);
        adsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adsList);
        listViewAds.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadAdsFromFirestore();
    }

    private void loadAdsFromFirestore() {
        db.collection("ads")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(AdsActivity.this, "Error al cargar anuncios.", Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", error.getMessage());
                            return;
                        }

                        if (value != null) {
                            adsList.clear();
                            for (QueryDocumentSnapshot doc : value) {
                                String title = doc.getString("title");
                                String description = doc.getString("description");
                                String adText = (title != null ? title : "Sin título") + "\n" +
                                        (description != null ? description : "Sin descripción");
                                adsList.add(adText);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
