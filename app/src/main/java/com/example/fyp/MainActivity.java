package com.example.fyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView Tv1, Tv2, Tv3;

    ImageView logo_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo_image = findViewById(R.id.logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(),home_page.class));

            }
        },3000);

    }
}
