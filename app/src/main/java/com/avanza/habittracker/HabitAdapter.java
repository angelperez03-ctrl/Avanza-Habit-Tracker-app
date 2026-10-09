package com.avanza.habittracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;

import com.avanza.habittracker.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avanza.habittracker.models.Habit;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Set;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private Context context;
    private final ArrayList<Habit> habits;

    private OnHabitCompletedListener completionListener;

    public interface OnHabitCompletedListener {
        void onHabitCompletionChanged();
    }

    public HabitAdapter(
            Context context,
            ArrayList<Habit> habitList,
            OnHabitCompletedListener completionListener) {

        this.context = context;
        this.habits = habitList;
        this.completionListener = completionListener;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.habit_item, parent, false);

        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull HabitViewHolder holder,
            int position) {

        Habit habit = habits.get(position);

        holder.txtHabitName.setText(habit.getName());
        holder.txtHabitDescription.setText(habit.getDescription());

        DatabaseHelper databaseHelper =
                new DatabaseHelper(holder.itemView.getContext());

        updateStreakText(
                holder,
                habit,
                databaseHelper
        );

        holder.txtFrequency.setText(
                habit.getFrequency()
        );

        String today = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(new Date());


        // Update week bars
        updateWeekBars(
                holder,
                habit,
                databaseHelper
        );

        boolean completed =
                databaseHelper.isHabitCompletedForDate(
                        habit.getId(),
                        today
                );
        if (completed) {

            holder.btnHabitComplete.setBackgroundResource(
                    R.drawable.circle_complete
            );

        } else {

            holder.btnHabitComplete.setBackgroundResource(
                    R.drawable.circle_incomplete
            );
        }

        holder.btnHabitComplete.setOnClickListener(v -> {

            boolean alreadyCompleted =
                    databaseHelper.isHabitCompletedForDate(
                            habit.getId(),
                            today
                    );

            if (alreadyCompleted) {

                // ----------------------------
                // UNDO COMPLETION
                // ----------------------------

                int result =
                        databaseHelper.deleteHabitCompletion(
                                habit.getId(),
                                today
                        );

                updateStreakText(
                        holder,
                        habit,
                        databaseHelper
                );

                if (result > 0) {

                    holder.btnHabitComplete.setBackgroundResource(
                            R.drawable.circle_incomplete
                    );

                    updateWeekBars(
                            holder,
                            habit,
                            databaseHelper
                    );

                    if (completionListener != null) {
                        completionListener.onHabitCompletionChanged();
                    }
                }

            } else {

                // ----------------------------
                // COMPLETE HABIT
                // ----------------------------

                long result =
                        databaseHelper.addHabitCompletion(
                                habit.getId(),
                                today,
                                1
                        );

                if (result != -1) {

                    holder.btnHabitComplete.setBackgroundResource(
                            R.drawable.circle_complete
                    );

                    updateWeekBars(
                            holder,
                            habit,
                            databaseHelper
                    );

                    updateStreakText(
                            holder,
                            habit,
                            databaseHelper
                    );

                    if (completionListener != null) {
                        completionListener.onHabitCompletionChanged();
                    }
                }
            }
        });

        // Edit habit pencil button
        holder.btnEditHabit.setOnClickListener(v -> {

            Intent intent = new Intent(
                    holder.itemView.getContext(),
                    AvanzaEditHabitActivity.class
            );

            intent.putExtra("HABIT_ID", habit.getId());

            holder.itemView.getContext().startActivity(intent);
        });
    }

    private void updateWeekBars(
            HabitViewHolder holder,
            Habit habit,
            DatabaseHelper databaseHelper) {

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        Calendar monday = Calendar.getInstance();

        int currentDay =
                monday.get(Calendar.DAY_OF_WEEK);

        int daysFromMonday =
                (currentDay + 5) % 7;

        monday.add(
                Calendar.DAY_OF_MONTH,
                -daysFromMonday
        );

        String[] weekDates = new String[7];

        Calendar day =
                (Calendar) monday.clone();

        for (int i = 0; i < 7; i++) {

            weekDates[i] =
                    dateFormat.format(day.getTime());

            day.add(
                    Calendar.DAY_OF_MONTH,
                    1
            );
        }

        Set<String> completedDates =
                databaseHelper.getHabitCompletionDatesForRange(
                        habit.getId(),
                        weekDates[0],
                        weekDates[6]
                );

        View[] dayProgressViews = {
                holder.barMonday,
                holder.barTuesday,
                holder.barWednesday,
                holder.barThursday,
                holder.barFriday,
                holder.barSaturday,
                holder.barSunday
        };

        for (int i = 0; i < 7; i++) {

            if (completedDates.contains(weekDates[i])) {

                dayProgressViews[i].setBackgroundResource(
                        R.drawable.day_bar_complete
                );

            } else {

                dayProgressViews[i].setBackgroundResource(
                        R.drawable.day_bar_incomplete
                );
            }
        }
    }

    private void updateStreakText(
            HabitViewHolder holder,
            Habit habit,
            DatabaseHelper databaseHelper) {

        int streak =
                calculateCurrentStreak(
                        habit,
                        databaseHelper
                );

        holder.txtHabitStreak.setText(
                "Streak: " + streak + "d"
        );
    }

    private int calculateCurrentStreak(
            Habit habit,
            DatabaseHelper databaseHelper) {

        ArrayList<String> completionDates =
                databaseHelper.getAllCompletionDatesForHabit(
                        habit.getId()
                );

        if (completionDates.isEmpty()) {
            return 0;
        }

        Set<String> completedDates =
                new HashSet<>(completionDates);

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        if (habit.getFrequency().equals("Weekly")) {

            return calculateWeeklyStreak(
                    completedDates,
                    dateFormat
            );
        }

        Calendar today =
                Calendar.getInstance();

        Calendar currentDate =
                getMostRecentScheduledDate(
                        today,
                        habit.getFrequency()
                );

        String currentDateString =
                dateFormat.format(
                        currentDate.getTime()
                );

        // If today is a scheduled day but the user
        // has not completed it yet, do not break
        // yesterday's streak yet.
        boolean currentDateIsToday =
                dateFormat.format(today.getTime())
                        .equals(currentDateString);

        if (currentDateIsToday
                && !completedDates.contains(currentDateString)) {

            currentDate =
                    getPreviousScheduledDate(
                            currentDate,
                            habit.getFrequency()
                    );
        }

        int streak = 0;

        while (completedDates.contains(
                dateFormat.format(currentDate.getTime()))) {

            streak++;

            currentDate =
                    getPreviousScheduledDate(
                            currentDate,
                            habit.getFrequency()
                    );
        }

        return streak;
    }

    private Calendar getMostRecentScheduledDate(
            Calendar date,
            String frequency) {

        Calendar scheduledDate =
                (Calendar) date.clone();

        if (frequency.equals("Weekdays")) {

            while (scheduledDate.get(Calendar.DAY_OF_WEEK)
                    == Calendar.SATURDAY
                    ||
                    scheduledDate.get(Calendar.DAY_OF_WEEK)
                            == Calendar.SUNDAY) {

                scheduledDate.add(
                        Calendar.DAY_OF_MONTH,
                        -1
                );
            }

        } else if (frequency.equals("Weekends")) {

            while (scheduledDate.get(Calendar.DAY_OF_WEEK)
                    != Calendar.SATURDAY
                    &&
                    scheduledDate.get(Calendar.DAY_OF_WEEK)
                            != Calendar.SUNDAY) {

                scheduledDate.add(
                        Calendar.DAY_OF_MONTH,
                        -1
                );
            }
        }

        return scheduledDate;
    }

    private Calendar getPreviousScheduledDate(
            Calendar date,
            String frequency) {

        Calendar previous =
                (Calendar) date.clone();

        switch (frequency) {

            case "Daily":

                previous.add(
                        Calendar.DAY_OF_MONTH,
                        -1
                );

                break;

            case "Weekdays":

                do {

                    previous.add(
                            Calendar.DAY_OF_MONTH,
                            -1
                    );

                } while (
                        previous.get(Calendar.DAY_OF_WEEK)
                                == Calendar.SATURDAY
                                ||
                                previous.get(Calendar.DAY_OF_WEEK)
                                        == Calendar.SUNDAY
                );

                break;

            case "Weekends":

                do {

                    previous.add(
                            Calendar.DAY_OF_MONTH,
                            -1
                    );

                } while (
                        previous.get(Calendar.DAY_OF_WEEK)
                                != Calendar.SATURDAY
                                &&
                                previous.get(Calendar.DAY_OF_WEEK)
                                        != Calendar.SUNDAY
                );

                break;
        }

        return previous;
    }

    private int calculateWeeklyStreak(
            Set<String> completedDates,
            SimpleDateFormat dateFormat) {

        Set<String> completedWeeks =
                new HashSet<>();

        for (String completionDate : completedDates) {

            try {

                Calendar date =
                        Calendar.getInstance();

                date.setTime(
                        dateFormat.parse(completionDate)
                );

                int currentDay =
                        date.get(Calendar.DAY_OF_WEEK);

                int daysFromMonday =
                        (currentDay + 5) % 7;

                date.add(
                        Calendar.DAY_OF_MONTH,
                        -daysFromMonday
                );

                completedWeeks.add(
                        dateFormat.format(
                                date.getTime()
                        )
                );

            } catch (Exception ignored) {
            }
        }

        Calendar week =
                Calendar.getInstance();

        int currentDay =
                week.get(Calendar.DAY_OF_WEEK);

        int daysFromMonday =
                (currentDay + 5) % 7;

        week.add(
                Calendar.DAY_OF_MONTH,
                -daysFromMonday
        );

        String currentWeek =
                dateFormat.format(
                        week.getTime()
                );

        // Current week isn't over yet, so don't
        // break the previous streak just because
        // it hasn't been completed yet.
        if (!completedWeeks.contains(currentWeek)) {

            week.add(
                    Calendar.DAY_OF_MONTH,
                    -7
            );
        }

        int streak = 0;

        while (completedWeeks.contains(
                dateFormat.format(week.getTime()))) {

            streak++;

            week.add(
                    Calendar.DAY_OF_MONTH,
                    -7
            );
        }

        return streak;
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public static class HabitViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtHabitName;
        TextView txtHabitDescription;
        TextView txtHabitStreak;
        TextView txtFrequency;

        ImageButton btnEditHabit;
        View btnHabitComplete;

        View barMonday;
        View barTuesday;
        View barWednesday;
        View barThursday;
        View barFriday;
        View barSaturday;
        View barSunday;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHabitName =
                    itemView.findViewById(R.id.txtHabitName);

            txtHabitDescription =
                    itemView.findViewById(R.id.txtHabitDescription);

            txtHabitStreak =
                    itemView.findViewById(R.id.txtHabitStreak);

            txtFrequency =
                    itemView.findViewById(R.id.txtFrequency);

            btnEditHabit =
                    itemView.findViewById(R.id.btnEditHabit);

            btnHabitComplete =
                    itemView.findViewById(R.id.btnHabitComplete);

            barMonday =
                    itemView.findViewById(R.id.barMonday);

            barTuesday =
                    itemView.findViewById(R.id.barTuesday);

            barWednesday =
                    itemView.findViewById(R.id.barWednesday);

            barThursday =
                    itemView.findViewById(R.id.barThursday);

            barFriday =
                    itemView.findViewById(R.id.barFriday);

            barSaturday =
                    itemView.findViewById(R.id.barSaturday);

            barSunday =
                    itemView.findViewById(R.id.barSunday);
        }
    }
}