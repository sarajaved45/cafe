package com.example.cafedesign;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class InteriorDesignsActivity extends AppCompatActivity {

    private FrameLayout[] containers;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interior_designs);

        // Get references to all containers
        containers = new FrameLayout[]{
                findViewById(R.id.container1),
                findViewById(R.id.container2),
                findViewById(R.id.container3),
                findViewById(R.id.container4),
                findViewById(R.id.container5),
        };

        // Set click listeners for each container
        containers[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InteriorDesignsActivity.this, ModernDesignActivity.class));
            }
        });

        containers[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InteriorDesignsActivity.this, OutdoorDesignActivity.class));
            }
        });

        containers[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InteriorDesignsActivity.this, CultureDesignActivity.class));
            }
        });

        containers[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InteriorDesignsActivity.this, IndustrialDesignActivity.class));
            }
        });

        containers[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InteriorDesignsActivity.this, ArtThemeActivity.class));
            }
        });
    }}
