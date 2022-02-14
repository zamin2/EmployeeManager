package com.example.firebaseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    // initializing elements
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // finding recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // setting layout manager to the recyclerview
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // calling out firebase realtime database to retrieve data
        // setting the employee class with the imported employee instance from the database
        // all the retrieved employee instances are inside the FirebaseRecyclerOptions variable 'options'
        FirebaseRecyclerOptions<EmployeeClass> options =
                new FirebaseRecyclerOptions.Builder<EmployeeClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("employees"), EmployeeClass.class)
                        .build();

        // setting the custom recyclerview adapter to the recyclerview with the imported 'options' variable
        recyclerAdapter = new RecyclerAdapter(options);
        recyclerView.setAdapter(recyclerAdapter);

        // finding the add floating button and assigning an OnClick listener
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        // every time we click on this add button, it takes us to the AddActivity activity from MainActivity
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);

            }
        });

    }


    // application is listening in real time for any changes in real time database and updating the recyclerview adapter
    @Override
    protected void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    // application is now stopped listening in real time for any changes in real time database and updating the recyclerview adapter
    @Override
    protected void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }
}