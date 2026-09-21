package com.avanza.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private MaterialButton reminderTimeDropDown;
    private SwitchMaterial notificationSwitch;

    private MaterialButton btnEdit;

    private MaterialButton btnSignOut;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avanza_settings);

        // Connect views
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        reminderTimeDropDown = findViewById(R.id.reminderTimeDropDown);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        btnEdit = findViewById(R.id.btnEdit);
        btnSignOut = findViewById(R.id.btnSignOut);

        //Edit Profile
        btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    AccountSettingsActivity.class
            );

            startActivity(intent);
        });

        // SharedPreferences for settings
        preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        // Load saved settings
        String savedReminderTime =
                preferences.getString("reminderTime", "12:00pm");

        boolean notificationsEnabled =
                preferences.getBoolean("notificationsEnabled", false);

        reminderTimeDropDown.setText(savedReminderTime);
        notificationSwitch.setChecked(notificationsEnabled);


        // ---------------------------------
        // NOTIFICATION SWITCH
        // ---------------------------------

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            preferences.edit()
                    .putBoolean("notificationsEnabled", isChecked)
                    .apply();

        });


        // ---------------------------------
        // REMINDER TIME DROPDOWN
        // ---------------------------------

        reminderTimeDropDown.setOnClickListener(view -> {

            PopupMenu popupMenu =
                    new PopupMenu(SettingsActivity.this, reminderTimeDropDown);

            popupMenu.getMenu().add("8:00am");
            popupMenu.getMenu().add("9:00am");
            popupMenu.getMenu().add("10:00am");
            popupMenu.getMenu().add("11:00am");
            popupMenu.getMenu().add("12:00pm");
            popupMenu.getMenu().add("1:00pm");
            popupMenu.getMenu().add("2:00pm");
            popupMenu.getMenu().add("3:00pm");
            popupMenu.getMenu().add("4:00pm");
            popupMenu.getMenu().add("5:00pm");
            popupMenu.getMenu().add("6:00pm");
            popupMenu.getMenu().add("7:00pm");
            popupMenu.getMenu().add("8:00pm");
            popupMenu.getMenu().add("9:00pm");

            popupMenu.setOnMenuItemClickListener(item -> {

                String selectedTime = item.getTitle().toString();

                // Display selected time
                reminderTimeDropDown.setText(selectedTime);

                // Save selected time
                preferences.edit()
                        .putString("reminderTime", selectedTime)
                        .apply();

                return true;
            });

            popupMenu.show();
        });


        //Sign out Button
        btnSignOut.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    loginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });


        // ---------------------------------
        // BOTTOM NAVIGATION
        // ---------------------------------

        // Show Settings as selected
        bottomNavigationView.setSelectedItemId(R.id.navSettings);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {

                Intent intent =
                        new Intent(SettingsActivity.this,
                                AvanzaDashboardActivity.class);

                startActivity(intent);
                finish();

                return true;
            }

            else if (itemId == R.id.navStats) {

                Intent intent =
                        new Intent(SettingsActivity.this,
                                StatsActivity.class);

                startActivity(intent);
                finish();

                return true;
            }

            else if (itemId == R.id.navSettings) {

                // Already on Settings screen
                return true;
            }

            return false;
        });
    }
}
