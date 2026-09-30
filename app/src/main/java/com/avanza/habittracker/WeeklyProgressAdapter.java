package com.avanza.habittracker;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avanza.habittracker.database.DatabaseHelper;
import com.avanza.habittracker.models.Habit;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

public class WeeklyProgressAdapter extends RecyclerView.Adapter<WeeklyProgressAdapter.WeeklyProgressViewHolder> {

    private final ArrayList<Habit> habits;
    private final DatabaseHelper databaseHelper;

    public WeeklyProgressAdapter(
            ArrayList<Habit> habits,
            DatabaseHelper databaseHelper) {

        this.habits = habits;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public WeeklyProgressViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_weekly_progress,
                        parent,
                        false
                );

        return new WeeklyProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull WeeklyProgressViewHolder holder,
            int position) {

        Habit habit = habits.get(position);

        holder.habitName.setText(
                habit.getName()
        );

        // Find Monday of the current week
        Calendar monday =
                Calendar.getInstance();

        int currentDay =
                monday.get(Calendar.DAY_OF_WEEK);

        int daysFromMonday =
                (currentDay + 5) % 7;

        monday.add(
                Calendar.DAY_OF_MONTH,
                -daysFromMonday
        );

        // Find Sunday
        Calendar sunday =
                (Calendar) monday.clone();

        sunday.add(
                Calendar.DAY_OF_MONTH,
                6
        );

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        String startDate =
                dateFormat.format(
                        monday.getTime()
                );

        String endDate =
                dateFormat.format(
                        sunday.getTime()
                );

        Set<String> completedDates =
                databaseHelper
                        .getHabitCompletionDatesForRange(
                                habit.getId(),
                                startDate,
                                endDate
                        );

        int expected =
                getExpectedWeeklyCompletions(
                        habit.getFrequency()
                );

        int completed =
                Math.min(
                        completedDates.size(),
                        expected
                );

        holder.habitCompletionCount.setText(
                completed + "/" + expected
        );

        // Real progress bar values
        if (expected > 0) {

            holder.habitProgressBar.setMax(
                    expected
            );

            holder.habitProgressBar.setProgressCompat(
                    completed,
                    false
            );

        } else {

            holder.habitProgressBar.setMax(1);
            holder.habitProgressBar.setProgressCompat(
                    0,
                    false
            );
        }
    }

    private int getExpectedWeeklyCompletions(
            String frequency) {

        switch (frequency) {

            case "Daily":
                return 7;

            case "Weekdays":
                return 5;

            case "Weekends":
                return 2;

            case "Weekly":
                return 1;

            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    static class WeeklyProgressViewHolder
            extends RecyclerView.ViewHolder {

        TextView habitName;
        TextView habitCompletionCount;

        LinearProgressIndicator habitProgressBar;

        public WeeklyProgressViewHolder(
                @NonNull View itemView) {

            super(itemView);

            habitName =
                    itemView.findViewById(
                            R.id.habitName
                    );

            habitCompletionCount =
                    itemView.findViewById(
                            R.id.habitCompletionCount
                    );

            habitProgressBar =
                    itemView.findViewById(
                            R.id.habitProgressBar
                    );
        }
    }
}
