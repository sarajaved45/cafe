package com.example.cafedesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity1 extends AppCompatActivity {
    BottomNavigationView bnview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        bnview = findViewById(R.id.navbar);

        bnview.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    loadfrag(new HomeFragment(), false);
                } else if (id == R.id.access) {
                    loadfrag(new AcessstoresFragment(), false);
                } else if (id == R.id.about) {
                    loadfrag(new AboutFragment(), false);
                }
                return true;
            }
        });

        // Initially load the HomeFragment
        loadfrag(new HomeFragment(), false);

        // Set the selected item in BottomNavigationView
        bnview.setSelectedItemId(R.id.home);
    }

    public void loadfrag(Fragment fragment, boolean flag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (flag) {
            fragmentTransaction.add(R.id.container, fragment);
        } else {
            fragmentTransaction.replace(R.id.container, fragment);
        }
        fragmentTransaction.commit();
    }
}
