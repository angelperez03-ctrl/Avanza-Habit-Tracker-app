package com.avanza.habittracker;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.avanza.habittracker.database.DatabaseHelper;
import com.avanza.habittracker.models.Habit;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class AvanzaEditHabitActivity extends AppCompatActivity {

    private int habitId;

    private DatabaseHelper databaseHelper;
    private Habit currentHabit;

    private TextInputEditText habitNameInput;
    private TextInputEditText habitDescriptionInput;

    private String selectedFrequency = "";

    private MaterialButton btnDeleteHabit;
    private MaterialButton btnCancelDelete;
    private MaterialButton btnConfirmDelete;

    private MaterialCardView deleteWarningCard;

    private TextView txtDeleteWarning;

    private MaterialButton dailyCard;
    private MaterialButton weekDaysCard;
    private MaterialButton weekendsCard;
    private MaterialButton weeklyCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avanza_edithabit);
        databaseHelper =
                new DatabaseHelper(AvanzaEditHabitActivity.this);

        // Get the habit ID sent from the HabitAdapter
        habitId = getIntent().getIntExtra("HABIT_ID", -1);

        if (habitId == -1) {
            finish();
            return;
        }

        currentHabit =
                databaseHelper.getHabitById(habitId);

        if (currentHabit == null) {

            Toast.makeText(
                    AvanzaEditHabitActivity.this,
                    "Habit could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        // Habit inputs
        habitNameInput =
                findViewById(R.id.editHabitNameInput);

        habitDescriptionInput =
                findViewById(R.id.editHabitDescriptionInput);

        habitNameInput.setText(
                currentHabit.getName()
        );

        habitDescriptionInput.setText(
                currentHabit.getDescription()
        );

        dailyCard = findViewById(R.id.dailyCard);
        weekDaysCard = findViewById(R.id.weekDaysCard);
        weekendsCard = findViewById(R.id.weekendsCard);
        weeklyCard = findViewById(R.id.weeklyCard);

        selectedFrequency =
                currentHabit.getFrequency();

        switch (selectedFrequency) {

            case "Daily":
                selectFrequencyButton(dailyCard);
                break;

            case "Weekdays":
                selectFrequencyButton(weekDaysCard);
                break;

            case "Weekends":
                selectFrequencyButton(weekendsCard);
                break;

            case "Weekly":
                selectFrequencyButton(weeklyCard);
                break;
        }


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

        MaterialButton btnSaveHabit =
                findViewById(R.id.btnSave);

        btnSaveHabit.setOnClickListener(v -> {

            String newName =
                    habitNameInput.getText()
                            .toString()
                            .trim();

            String newDescription =
                    habitDescriptionInput.getText()
                            .toString()
                            .trim();

            if (newName.isEmpty()) {

                Toast.makeText(
                        AvanzaEditHabitActivity.this,
                        "Please enter a habit name",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int result =
                    databaseHelper.updateHabit(
                            habitId,
                            newName,
                            newDescription,
                            selectedFrequency,
                            currentHabit.getStreak()
                    );

            if (result > 0) {

                Toast.makeText(
                        AvanzaEditHabitActivity.this,
                        "Habit updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        AvanzaEditHabitActivity.this,
                        "Habit could not be updated",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        MaterialButton btnCancelHabit =
                findViewById(R.id.btnCancel);

        btnCancelHabit.setOnClickListener(v -> {
            finish();
        });

        // Delete controls
        btnDeleteHabit =
                findViewById(R.id.btnDeleteHabit);

        btnCancelDelete =
                findViewById(R.id.btnCancelDelete);

        btnConfirmDelete =
                findViewById(R.id.btnConfirmDelete);

        deleteWarningCard =
                findViewById(R.id.deleteWarningCard);

        txtDeleteWarning =
                findViewById(R.id.txtDeleteWarning);


        // Small Delete button at top
        btnDeleteHabit.setOnClickListener(v -> {

            String habitName =
                    habitNameInput.getText().toString().trim();

            if (habitName.isEmpty()) {

                txtDeleteWarning.setText(
                        "Delete this habit? This can't be undone."
                );

            } else {

                txtDeleteWarning.setText(
                        "Delete " + habitName +
                                "? This can't be undone."
                );
            }

            // Show delete warning
            deleteWarningCard.setVisibility(View.VISIBLE);
        });


        // Cancel delete
        btnCancelDelete.setOnClickListener(v -> {

            deleteWarningCard.setVisibility(View.GONE);

        });


        // Confirm delete
        btnConfirmDelete.setOnClickListener(v -> {

            int result =
                    databaseHelper.deleteHabit(
                            habitId
                    );

            if (result > 0) {

                Toast.makeText(
                        AvanzaEditHabitActivity.this,
                        "Habit deleted successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        AvanzaEditHabitActivity.this,
                        "Habit could not be deleted",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        // Later:
        // loadHabitFromDatabase();
    }

    private void selectFrequencyButton(
            MaterialButton selectedButton) {

        int defaultColor =
                getColor(R.color.avanza_light_grey);

        int selectedColor =
                getColor(R.color.avanza_cyan);

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