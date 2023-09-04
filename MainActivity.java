package com.mod5.projecttwo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializng the UI elements
        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button submitBtn = findViewById(R.id.submitBtn);
        Button createAccountBtn = findViewById(R.id.createBtn);

        // created an onClickListener for the submit button
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(username, password);
                }
            }
        });

        // created an onClickListener for the create account button
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(username, password);
                }
            }

        });
    }

    // registerUser is a method used to create a new user
    private void registerUser(String username, String password) {
        // Creating the database class
        Database database = new Database(MainActivity.this);

        // CHecking to see if user already exists
        boolean isUserExists = database.checkUser(username, password);
        if(!isUserExists){
            // create new username and save it to the database
            boolean isUserAdded = database.addUser(username, password);
            if(isUserAdded){
                Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
        }

    }

    // loginUser is used to log in an existing user
    private void loginUser(String username, String password) {
        // starting the database
        Database database = new Database(MainActivity.this);

        // Check if the user exists in the database with the given username and passsword
        boolean isUserValid = database.checkUser(username, password);
        if(isUserValid){
            Toast.makeText(MainActivity.this, "LogIn Successful!", Toast.LENGTH_SHORT).show();
            //Navigate to the next activity
            Intent intent = new Intent(MainActivity.this, DataGridActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(MainActivity.this, "Invalid Username or password ", Toast.LENGTH_SHORT).show();
        }
    }
}