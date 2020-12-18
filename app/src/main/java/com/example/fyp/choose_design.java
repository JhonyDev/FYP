package com.example.fyp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class choose_design extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    ImageView kameez;
    ImageView shawal;
    ImageView frock;
    ImageView trouser;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    int selection;

    String TAG = "mytag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_design);

        recyclerView = findViewById(R.id.recyclerVIEW);
        kameez = findViewById(R.id.kameezIMG);
        shawal = findViewById(R.id.shawalIMG);
        frock = findViewById(R.id.frockIMG);
        trouser = findViewById(R.id.trouserIMG);
        recyclerViewAdapter = new RecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setList(getFrock());

        kameez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewAdapter.setList(getKameez());
            }
        });

        shawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewAdapter.setList(getShawl());
            }
        });
        frock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewAdapter.setList(getFrock());
            }
        });

        trouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewAdapter.setList(getTrouser());
            }
        });

    }

    public List<Integer> getKameez() {
        selection = 0;
        List<Integer> myList = new ArrayList<>();
        Resources resources = this.getResources();
        final int resourceId1 = resources.getIdentifier("kameez1", "drawable", this.getPackageName());
        final int resourceId2 = resources.getIdentifier("kameez2", "drawable", this.getPackageName());
        final int resourceId3 = resources.getIdentifier("kameez3", "drawable", this.getPackageName());
        final int resourceId4 = resources.getIdentifier("kameez4", "drawable", this.getPackageName());
        myList.add(resourceId1);
        myList.add(resourceId2);
        myList.add(resourceId3);
        myList.add(resourceId4);
        return myList;
//        resources.getDrawable(resourceId);
    }

    public List<Integer> getFrock() {
        selection = 1;
        List<Integer> myList = new ArrayList<>();
        Resources resources = this.getResources();
        final int resourceId1 = resources.getIdentifier("frock", "drawable", this.getPackageName());
        final int resourceId2 = resources.getIdentifier("frock1", "drawable", this.getPackageName());
        final int resourceId3 = resources.getIdentifier("frock2", "drawable", this.getPackageName());
        final int resourceId4 = resources.getIdentifier("frock3", "drawable", this.getPackageName());
        myList.add(resourceId1);
        myList.add(resourceId2);
        myList.add(resourceId3);
        myList.add(resourceId4);
        return myList;
//        resources.getDrawable(resourceId);
    }

    public List<Integer> getTrouser() {
        selection = 2;
        List<Integer> myList = new ArrayList<>();
        Resources resources = this.getResources();
        final int resourceId1 = resources.getIdentifier("trouser", "drawable", this.getPackageName());
        final int resourceId2 = resources.getIdentifier("trouser1", "drawable", this.getPackageName());
        final int resourceId3 = resources.getIdentifier("trouser2", "drawable", this.getPackageName());
        final int resourceId4 = resources.getIdentifier("trouser3", "drawable", this.getPackageName());
        myList.add(resourceId1);
        myList.add(resourceId2);
        myList.add(resourceId3);
        myList.add(resourceId4);
        return myList;
//        resources.getDrawable(resourceId);
    }

    public List<Integer> getShawl() {
        selection = 3;
        List<Integer> myList = new ArrayList<>();
        Resources resources = this.getResources();
        final int resourceId1 = resources.getIdentifier("shawal1", "drawable", this.getPackageName());
        final int resourceId2 = resources.getIdentifier("shawal2", "drawable", this.getPackageName());
        myList.add(resourceId1);
        myList.add(resourceId2);
        return myList;
//        resources.getDrawable(resourceId);
    }

    @Override
    public void onItemClick(View view, int position) {
        int resourceId = 0;
        switch (selection) {
            case 0:
                resourceId = getKameez().get(position);
                break;
            case 1:
                resourceId = getFrock().get(position);
                break;
            case 2:
                resourceId = getTrouser().get(position);
                break;
            case 3:
                resourceId = getShawl().get(position);
                break;
        }

        Intent i = new Intent(this, customize.class);
        i.putExtra("resourceId", resourceId);
        startActivity(i);
    }
}
