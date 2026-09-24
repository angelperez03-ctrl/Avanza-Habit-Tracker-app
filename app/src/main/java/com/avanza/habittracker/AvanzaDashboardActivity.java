package com.avanza.habittracker;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avanza.habittracker.database.DatabaseHelper;
import com.avanza.habittracker.models.Habit;
import com.avanza.habittracker.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import android.widget.TextView;

import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AvanzaDashboardActivity extends AppCompatActivity {

    private CircularProgressIndicator weeklyProgressCircle;
    private TextView txtProgressPercentage;
    private TextView txtHabitsCompleted;
    private TextView txtHabitsRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avanza_dashboard);

        int userId = getSharedPreferences(
                "AvanzaPrefs",
                MODE_PRIVATE
        ).getInt("loggedInUserId", -1);

        if (userId == -1) {
            Intent intent = new Intent(
                    AvanzaDashboardActivity.this,
                    loginActivity.class
            );
            startActivity(intent);
            finish();
            return;
        }

        RecyclerView habitRecyclerView =
                findViewById(R.id.habitRecyclerView);

        habitRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        DatabaseHelper databaseHelper =
                new DatabaseHelper(this);

        weeklyProgressCircle =
                findViewById(R.id.weeklyProgressCircle);

        txtProgressPercentage =
                findViewById(R.id.txtProgressPercentage);

        txtHabitsCompleted =
                findViewById(R.id.txtHabitsCompleted);

        txtHabitsRemaining =
                findViewById(R.id.txtHabitsRemaining);

        User user = databaseHelper.getUserById(userId);

        TextView greetingText = findViewById(R.id.txtGreeting);

        if (user!= null) {
            greetingText.setText(
                    getString(R.string.good_morning_user, user.getName())
            );
        }

        ArrayList<Habit> habitList = new ArrayList<>(
                databaseHelper.getAllHabitsByUser(userId)
        );

        updateDashboardProgress(
                databaseHelper,
                userId,
                habitList
        );

        HabitAdapter habitAdapter =
                new HabitAdapter(AvanzaDashboardActivity.this,
                        habitList,
                        () -> updateDashboardProgress(
                                databaseHelper,
                                userId,
                                habitList
                        )
                );

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
    private void updateDashboardProgress(
            DatabaseHelper databaseHelper,
            int userId,
            ArrayList<Habit> habitList) {

        String today = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(new Date());

        int totalHabits = habitList.size();

        int completedHabits =
                databaseHelper.getCompletedHabitCountForDate(
                        userId,
                        today
                );

        int remainingHabits =
                totalHabits - completedHabits;

        int progressPercentage = 0;

        if (totalHabits > 0) {
            progressPercentage =
                    (completedHabits * 100) / totalHabits;
        }

        weeklyProgressCircle.setProgress(progressPercentage);


        txtProgressPercentage.setText(
                progressPercentage + "%"
        );

        txtHabitsCompleted.setText(
                completedHabits +
                        " of " +
                        totalHabits +
                        " done"
        );

        txtHabitsRemaining.setText(
                remainingHabits +
                        " remaining"
        );
    }

}