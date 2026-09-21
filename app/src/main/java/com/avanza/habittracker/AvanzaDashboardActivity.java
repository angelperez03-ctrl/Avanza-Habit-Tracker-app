package com.avanza.habittracker;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avanza.habittracker.models.Habit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import android.widget.ImageButton;

import java.util.ArrayList;

public class AvanzaDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avanza_dashboard);

        int userId = getSharedPreferences(
                "AvanzaPrefs",
                MODE_PRIVATE
        ).getInt("loggedInUserId", -1);

        RecyclerView habitRecyclerView =
                findViewById(R.id.habitRecyclerView);

        habitRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        ArrayList<Habit> habits = new ArrayList<>();

        HabitAdapter habitAdapter =
                new HabitAdapter(habits);

        habitRecyclerView.setAdapter(habitAdapter);

        // ----------------------------
        // New Habit Button
        // ----------------------------

        MaterialButton btnNewHabit = findViewById(R.id.btnNewHabit);

        btnNewHabit.setOnClickListener(v -> {
            Intent intent = new Intent(AvanzaDashboardActivity.this, AvanzaNewHabitActivity.class);
            startActivity(intent);
        });

        // ----------------------------
        // BOTTOM NAVIGATION
        // ----------------------------

        BottomNavigationView bottomNavigationView =
                findViewById(R.id.bottomNavigation);

        // Dashboard is the Home screen
        bottomNavigationView.setSelectedItemId(R.id.navHome);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {

                // Already on the Home screen
                return true;

            } else if (itemId == R.id.navStats) {

                Intent intent = new Intent(
                        AvanzaDashboardActivity.this,
                        StatsActivity.class
                );

                startActivity(intent);

                return true;

            } else if (itemId == R.id.navSettings) {

                Intent intent = new Intent(
                        AvanzaDashboardActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                return true;
            }
            return false;

        });
    }
}