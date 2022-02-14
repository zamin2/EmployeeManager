package com.example.firebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    // initializing
    EditText name, role,email,imageURL;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // finding all the elements and assigning them to variables
        name = (EditText) findViewById(R.id.NameInput);
        role = (EditText) findViewById(R.id.RoleInput);
        email = (EditText) findViewById(R.id.EmailInput);
        imageURL = (EditText) findViewById(R.id.ImageInput);

        save = (Button) findViewById(R.id.AddButton);

        // listening for click events for the save button and performing AddData() function for every click event
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddData();
            }
        });
    }

    private void AddData(){
        // input validation check
        if (name.getText().toString().equals("") || role.getText().toString().equals("") || email.getText().toString().equals("")){
            Toast.makeText(AddActivity.this, "Please fill out name, role and email fields before adding", Toast.LENGTH_SHORT).show();
        }
        // if input validation passes then data from the form will be used to add a new employee instance
        else {
            // creating a hashmap and setting 4 different key value pairs
            Map<String, Object> map = new HashMap<>();
            map.put("name", name.getText().toString());
            map.put("role", role.getText().toString());
            map.put("email", email.getText().toString());
            map.put("imageURL", imageURL.getText().toString());

            // calling the firebase realtime database and pushing the new hashmap into the employee root
            FirebaseDatabase.getInstance().getReference().child("employees").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        // if add was successful, it will show a toast message that is successful and clear out the form and redirect to the
                        // Main Activity
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();

                            name.setText("");
                            role.setText("");
                            email.setText("");
                            imageURL.setText("");
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {

                        // if add was not successful, it will show a toast message that add failed
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddActivity.this, "Error in adding new employee record", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // if we pressed the back button, it will take us back to the main activity
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        // do something on back.

    }
}