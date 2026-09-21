package com.avanza.habittracker;

import android.os.Bundle;

import android.graphics.Matrix;
import android.graphics.SweepGradient;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.avanza.habittracker.database.DatabaseHelper;
import com.avanza.habittracker.models.User;
import com.avanza.habittracker.utils.PasswordUtils;
import com.google.android.material.textfield.TextInputEditText;

public class loginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        Button btnSignIn = findViewById(R.id.btnSignIn);

        //Database
        databaseHelper = new DatabaseHelper(loginActivity.this);

        emailInput = findViewById(R.id.editEmail);
        passwordInput = findViewById(R.id.editPassword);

        btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    loginActivity.this,
                    AvanzaDashboardActivity.class
            );

            startActivity(intent);
        });
        TextView txtAvanza = findViewById(R.id.txtAvanza);

        txtAvanza.post(() -> {

            int blue = ContextCompat.getColor(this, R.color.accents_blue);
            int cyan = ContextCompat.getColor(this, R.color.avanza_cyan);
            int green = ContextCompat.getColor(this, R.color.avanza_green);

            float centerX = txtAvanza.getWidth() / 2f;
            float centerY = txtAvanza.getHeight() / 2f;

            SweepGradient gradient = new SweepGradient(
                    centerX,
                    centerY,
                    new int[]{
                            blue,
                            cyan,
                            green,
                            blue
                    },
                    null
            );

            // Angular gradient rotation
            Matrix matrix = new Matrix();
            matrix.setRotate(180f, centerX, centerY);
            gradient.setLocalMatrix(matrix);

            txtAvanza.getPaint().setShader(gradient);
        });
        TextView txtCreateAccount = findViewById(R.id.txtCreateAccount);

        txtCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(loginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        TextView txtChangePassword = findViewById(R.id.txtForgotPassword);

        txtChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(loginActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });

        //Login Event Listener
        btnSignIn.setOnClickListener(v -> {
                    String email = emailInput.getText().toString().trim();
                    String password = passwordInput.getText().toString();

                    //Gets user by email
                    User user = databaseHelper.getUserByEmail(email);

                    //Checks if email and password fields are empty
                    if (email.isEmpty() || password.isEmpty()) {

                        Toast.makeText(
                                loginActivity.this,
                                "Please enter your email and password",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    //Checks if user exists
                    if (user == null) {

                        Toast.makeText(
                                loginActivity.this,
                                "Account not found",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    //Password verification
                    boolean passwordMatches = PasswordUtils.verifyPassword(
                            password,
                            user.getPasswordHash(),
                            user.getPasswordSalt()
                    );

                    //Successful login
                    if (passwordMatches) {

                        Toast.makeText(
                                loginActivity.this,
                                "Login successful",
                                Toast.LENGTH_SHORT
                        ).show();

                        getSharedPreferences("AvanzaPrefs", MODE_PRIVATE)
                                .edit()
                                .putInt("loggedInUserId", user.getId())
                                .apply();

                        Intent intent = new Intent(
                                loginActivity.this,
                                AvanzaDashboardActivity.class
                        );

                        startActivity(intent);
                        finish();

                    } else {
                        //Incorrect password
                        Toast.makeText(
                                loginActivity.this,
                                "Incorrect password",
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