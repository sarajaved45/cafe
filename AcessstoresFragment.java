package com.example.cafedesign;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AcessstoresFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_acessstores, container, false);

        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the URL of the website for Button 1
                String websiteUrl = "https://www.olx.com.pk/item/restaurants-furniture-cafe-furniture-sofa-dining-set-03002280913-iid-1077199231";

                // Create an Intent to open a web browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define the URL of the website for Button 2
                String websiteUrl = "https://www.olx.com.pk/item/sofa-repair-sofa-cum-bed-sofa-set-fabric-change-sofa-poshish-iid-1085681181"; // Replace this with the desired URL

                // Create an Intent to open a web browser
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
                startActivity(intent);
            }
        });

        return view;
    }
}
