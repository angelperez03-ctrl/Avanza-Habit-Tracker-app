package com.avanza.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import android.content.Intent;

import com.avanza.habittracker.database.DatabaseHelper;
import com.avanza.habittracker.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class AccountSettingsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private int userId;

    private TextInputEditText editFullName;
    private TextInputEditText editEmail;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avanza_accountsettings);

        databaseHelper = new DatabaseHelper(this);

        userId = getSharedPreferences(
                "AvanzaPrefs",
                MODE_PRIVATE
        ).getInt("loggedInUserId", -1);


        if (userId == -1) {
            finish();
            return;
        }

        editFullName =
                findViewById(R.id.editFullName);

        editEmail =
                findViewById(R.id.editEmail);

        MaterialButton btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        bottomNavigationView =
                findViewById(R.id.bottomNavigation);

        loadUserProfile();

        btnUpdateProfile.setOnClickListener(v -> {
            updateUserProfile();
        });

        setupBottomNavigation();
    }

    private void loadUserProfile() {

        User user =
                databaseHelper.getUserById(userId);

        if (user != null) {

            editFullName.setText(
                    user.getName()
            );

            editEmail.setText(
                    user.getEmail()
            );
        }
    }

    private void updateUserProfile() {

        String newName =
                Objects.requireNonNull(editFullName.getText())
                        .toString()
                        .trim();

        String newEmail =
                Objects.requireNonNull(editEmail.getText())
                        .toString()
                        .trim();

        if (newName.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter your name",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (newEmail.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter your email",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {

            Toast.makeText(
                    this,
                    "Please enter a valid email",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        User emailOwner =
                databaseHelper.getUserByEmail(newEmail);

        if (emailOwner != null
                && emailOwner.getId() != userId) {

            Toast.makeText(
                    this,
                    "That email is already in use",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int result =
                databaseHelper.updateUserProfile(
                        userId,
                        newName,
                        newEmail
                );

        if (result > 0) {

            Toast.makeText(
                    this,
                    "Profile updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Profile could not be updated",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
    private void setupBottomNavigation() {

        bottomNavigationView.setSelectedItemId(
                R.id.navSettings
        );

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {

                Intent intent = new Intent(
                        AccountSettingsActivity.this,
                        AvanzaDashboardActivity.class
                );

                startActivity(intent);
                finish();

                return true;

            } else if (itemId == R.id.navStats) {

                Intent intent = new Intent(
                        AccountSettingsActivity.this,
                        StatsActivity.class
                );

                startActivity(intent);
                finish();

                return true;

            } else if (itemId == R.id.navSettings) {

                Intent intent = new Intent(
                        AccountSettingsActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                finish();

                return true;
            }

            return false;
        });
    }
}
