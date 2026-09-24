package com.avanza.habittracker;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.avanza.habittracker.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import android.content.Intent;
import android.widget.Toast;


public class AvanzaNewHabitActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextInputEditText habitNameInput;
    private TextInputEditText habitDescriptionInput;

    private MaterialButton dailyCard;
    private MaterialButton weekDaysCard;
    private MaterialButton weekendsCard;
    private MaterialButton weeklyCard;

    private String selectedFrequency = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avanza_newhabit);

        databaseHelper = new DatabaseHelper(AvanzaNewHabitActivity.this);

        habitNameInput = findViewById(R.id.habitNameInput);

        habitDescriptionInput = findViewById(R.id.habitDescriptionInput);

        dailyCard = findViewById(R.id.dailyCard);
        weekDaysCard = findViewById(R.id.weekDaysCard);
        weekendsCard = findViewById(R.id.weekendsCard);
        weeklyCard = findViewById(R.id.weeklyCard);

        MaterialButton btnAddHabit = findViewById(R.id.btnAddHabit);

        int userId = getSharedPreferences(
                "AvanzaPrefs",
                MODE_PRIVATE
        ).getInt("loggedInUserId", -1);

        dailyCard.setOnClickListener(v -> {
            selectedFrequency = "Daily";
            selectFrequencyButton(dailyCard);
        });

        weekDaysCard.setOnClickListener(v -> {
            selectedFrequency = "Weekdays";
            selectFrequencyButton(weekDaysCard);
        });

        weekendsCard.setOnClickListener(v -> {
            selectedFrequency = "Weekends";
            selectFrequencyButton(weekendsCard);
        });

        weeklyCard.setOnClickListener(v -> {
            selectedFrequency = "Weekly";
            selectFrequencyButton(weeklyCard);
        });

        btnAddHabit.setOnClickListener(v -> {

            String habitName =
                    habitNameInput.getText().toString().trim();

            String habitDescription =
                    habitDescriptionInput.getText().toString().trim();

            if (userId == -1) {

                Toast.makeText(
                        AvanzaNewHabitActivity.this,
                        "No logged-in user found",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (habitName.isEmpty()){

                Toast.makeText(
                        AvanzaNewHabitActivity.this,
                        "Please enter a habit name",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (selectedFrequency.isEmpty()) {

                Toast.makeText(
                        AvanzaNewHabitActivity.this,
                        "Please select a frequency",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            long result = databaseHelper.addHabit(
                    userId,
                    habitName,
                    habitDescription,
                    selectedFrequency
            );

            if (result != -1) {

                Toast.makeText(
                        AvanzaNewHabitActivity.this,
                        "Habit created successfully",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        AvanzaNewHabitActivity.this,
                        AvanzaDashboardActivity.class
                );

                startActivity(intent);
                finish();

            } else {

                Toast.makeText(
                        AvanzaNewHabitActivity.this,
                        "Habit could not be created",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    private void selectFrequencyButton(MaterialButton selectedButton) {

        int defaultColor = getColor(R.color.avanza_light_grey);
        int selectedColor = getColor(R.color.avanza_cyan);

        dailyCard.setBackgroundTintList(
                ColorStateList.valueOf(defaultColor)
        );

        weekDaysCard.setBackgroundTintList(
                ColorStateList.valueOf(defaultColor)
        );

        weekendsCard.setBackgroundTintList(
                ColorStateList.valueOf(defaultColor)
        );

        weeklyCard.setBackgroundTintList(
                ColorStateList.valueOf(defaultColor)
        );

        selectedButton.setBackgroundTintList(
                ColorStateList.valueOf(selectedColor)
        );
    }
}