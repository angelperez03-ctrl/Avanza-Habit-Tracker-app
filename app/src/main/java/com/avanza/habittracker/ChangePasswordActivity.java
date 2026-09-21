package com.avanza.habittracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
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

import org.w3c.dom.Text;

public class ChangePasswordActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextInputEditText emailInput;
    private TextInputEditText newPasswordInput;
    private TextInputEditText confirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        TextView txtChangePassword = findViewById(R.id.txtLogin);

        //Database
        databaseHelper = new DatabaseHelper(ChangePasswordActivity.this);

        emailInput = findViewById(R.id.editEmail);
        newPasswordInput = findViewById(R.id.editPassword);
        confirmPasswordInput = findViewById(R.id.editConfirmPassword);

        Button btnChangePassword = findViewById(R.id.btnUpdatePassword);

        btnChangePassword.setOnClickListener(v -> {

            String email = emailInput.getText().toString().trim();

            String newPassword = newPasswordInput.getText().toString();

            String confirmPassword = confirmPasswordInput.getText().toString();

            if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {

                Toast.makeText(
                        ChangePasswordActivity.this,
                        "Please fill out all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (!newPassword.equals(confirmPassword)) {

                Toast.makeText(
                        ChangePasswordActivity.this,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            User user = databaseHelper.getUserByEmail(email);

            if (user == null) {

                Toast.makeText(
                        ChangePasswordActivity.this,
                        "Account not found",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            String newSalt =
                    PasswordUtils.generateSalt();

            String newPasswordHash =
                    PasswordUtils.hashPassword(
                            newPassword,
                            newSalt
                    );

            int result =
                    databaseHelper.updateUserPassword(
                            user.getId(),
                            newPasswordHash,
                            newSalt
                    );

            if (result > 0) {

                Toast.makeText(
                        ChangePasswordActivity.this,
                        "Password changed successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        ChangePasswordActivity.this,
                        "Password could not be changed",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        txtChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePasswordActivity.this, loginActivity.class);
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}