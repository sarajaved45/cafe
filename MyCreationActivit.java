package com.example.cafedesign;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyCreationActivit extends AppCompatActivity {

    private GridView creationsGridView;
    private List<Creation> creationsList;
    private CreationsAdapter creationsAdapter;
    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_creation);

        // Initialize Firestore
        mDatabase = FirebaseFirestore.getInstance();

        // Initialize views
        creationsGridView = findViewById(R.id.gridView);

        // Initialize list for creations
        creationsList = new ArrayList<>();
        creationsAdapter = new CreationsAdapter(this, creationsList);
        creationsGridView.setAdapter(creationsAdapter);

        // Load creations from Firestore
        loadCreations();
    }

    private void loadCreations() {
        mDatabase.collection("creations")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Creation creation = documentSnapshot.toObject(Creation.class);
                            creationsList.add(creation);
                        }
                        creationsAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MyCreationActivity", "Error loading creations", e);
                    }
                });
    }
}

