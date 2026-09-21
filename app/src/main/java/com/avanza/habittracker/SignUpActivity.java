package com.avanza.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.avanza.habittracker.database.DatabaseHelper;
import com.avanza.habittracker.models.User;
import com.avanza.habittracker.utils.PasswordUtils;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        //Database
        databaseHelper = new DatabaseHelper(SignUpActivity.this);


        //Input fields
        nameInput = findViewById(R.id.editFullName);
        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.editPassword);


        //Buttons / Text
        Button signUpButton = findViewById(R.id.btnSignIn);
        TextView txtCreateAccount = findViewById(R.id.txtLogin);

        txtCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, loginActivity.class);
            startActivity(intent);
        });

        // Create account
        signUpButton.setOnClickListener(v -> {

            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            // Basic validation
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        SignUpActivity.this,
                        "Please fill out all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Check if email is already registered
            User existingUser = databaseHelper.getUserByEmail(email);

            if (existingUser != null) {

                Toast.makeText(
                        SignUpActivity.this,
                        "An account with this email already exists",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            //Generate password salt
            String salt = PasswordUtils.generateSalt();

            // Hash password
            String passwordHash = PasswordUtils.hashPassword(
                    password,
                    salt
            );

            //Add user to SQLite
            long result = databaseHelper.addUser(
                    name,
                    email,
                    passwordHash,
                    salt
            );

            if (result != -1) {

                Toast.makeText(
                        this,
                        "Account created successfully",
                        Toast.LENGTH_SHORT
                ).show();

                // Send user to login screen
                Intent intent = new Intent(
                        SignUpActivity.this,
                        loginActivity.class
                );

                startActivity(intent);
                finish();

            } else {

                Toast.makeText(
                        this,
                        "Account could not be created",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

