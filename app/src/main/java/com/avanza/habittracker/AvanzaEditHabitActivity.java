package com.avanza.habittracker;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class AvanzaEditHabitActivity extends AppCompatActivity {

    private int habitId;

    private TextInputEditText habitNameInput;
    private TextInputEditText habitDescriptionInput;

    private MaterialButton btnDeleteHabit;
    private MaterialButton btnCancelDelete;
    private MaterialButton btnConfirmDelete;

    private MaterialCardView deleteWarningCard;

    private TextView txtDeleteWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avanza_edithabit);

        // Get the habit ID sent from the HabitAdapter
        habitId = getIntent().getIntExtra("HABIT_ID", -1);

        if (habitId == -1) {
            finish();
            return;
        }

        // Habit inputs
        habitNameInput =
                findViewById(R.id.editHabitNameInput);

        habitDescriptionInput =
                findViewById(R.id.editHabitDescriptionInput);

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

            // SQLite deletion will go here later
            // Example:
            // databaseHelper.deleteHabit(habitId);

            // Close Edit Habit screen after deletion
            // finish();

        });


        // Later:
        // loadHabitFromDatabase();
    }
}