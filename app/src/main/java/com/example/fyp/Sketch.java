package com.example.fyp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class Sketch extends AppCompatActivity implements View.OnClickListener {

    private PaintView paintView;
    private int defaultColor;
    private final int PICK_IMAGE_REQUEST = 1;
    private final int STORAGE_PERMISSION_CODE = 1;
    private static final int PERMISSION_CODE = 1001;
    private ImageButton ibImport;
    private ImageButton ibRedo;
    private ImageButton ibUndo;
    private ImageButton ibSave;
    private ImageButton ibClear;
    private SeekBar seekBar;
    private TextView textView;

    private void initAndSetViews() {
        paintView = findViewById(R.id.paintView);
        ibImport = findViewById(R.id.btn_import);
        ibRedo = findViewById(R.id.ib_redo);
        ibUndo = findViewById(R.id.ib_undo);
        ibSave = findViewById(R.id.ib_save);
        ibClear = findViewById(R.id.ib_clear);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.current_pen_size);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        paintView.initialise(displayMetrics);
        textView.setText("Pen size: " + seekBar.getProgress());
        initListeners();
    }

    private void initListeners() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paintView.setStrokeWidth(seekBar.getProgress());
                textView.setText("Pen size: " + seekBar.getProgress());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void accessGallery() {
        paintView.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission not granted, request it.
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //show popup for runtime permission//
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                // permission already granted//
                chooseImage();
            }
        } else {
            chooseImage();
        }
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);
        initAndSetViews();
        setListeners();
    }

    private void setListeners() {
        ibImport.setOnClickListener(this);
        ibRedo.setOnClickListener(this);
        ibUndo.setOnClickListener(this);
        ibSave.setOnClickListener(this);
        ibClear.setOnClickListener(this);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Needed to save image")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Sketch.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Access granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Access denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = findViewById(R.id.iv_preview);
                imageView.setImageBitmap(bitmap);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                paintView.setBackground(d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ibImport) {
            accessGallery();
        }

        if (v == ibRedo) {
            paintView.redo();
        }

        if (v == ibSave) {
            if (ContextCompat.checkSelfPermission(Sketch.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestStoragePermission();
            }
            paintView.saveImage(Sketch.this);
        }

        if (v == ibUndo) {
            paintView.undo();
        }

        if (v == ibClear) {
            paintView.clear();
        }
    }
}
