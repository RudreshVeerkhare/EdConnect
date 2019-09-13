package com.oop.edconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedHashMap;

public class ListData extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar2);
        FirebaseAuth.getInstance().signOut();


        new FirebaseDatabaseHelper("Student").readDataStudent(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(LinkedHashMap<String, Object> map) {
                progressBar.setVisibility(View.INVISIBLE);
                new RecyclerViewConfig(recyclerView, getApplicationContext(), map);

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });
    }
}
