package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class home_page extends AppCompatActivity {

    TextView Tview4, Tview5;
    Button btn_category, btn_custom, btn_sketch, btn_import;
    Spinner spinner_category;
    ImageView image_select, image_sketch;


    private static final int PERMISSION_CODE = 1001;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Tview4 = findViewById(R.id.Tv4);
        Tview5 = findViewById(R.id.Tv5);

        image_select = findViewById(R.id.ImageView1);
        image_sketch = findViewById(R.id.imageView2);
        image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent category = new Intent(home_page.this, choose_design.class);
                startActivity(category);

            }
        });
        
        image_sketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sketch = new Intent(home_page.this, Sketch.class);
                startActivity(sketch);
            }
        });
    }
}


