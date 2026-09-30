package com.avanza.habittracker;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import com.avanza.habittracker.database.DatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import android.widget.ProgressBar;
import android.view.LayoutInflater;

import com.avanza.habittracker.models.Habit;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
public class StatsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper databaseHelper;
    private int userId;

    private TextView completionRatePercentage;
    private TextView bestStreakNumber;

    private TextView totalHabitsNumber;

    private TextView completedHabits;

    private RecyclerView weeklyProgressRecyclerView;

    private ArrayList<Habit> weeklyHabitList;

    private WeeklyProgressAdapter weeklyProgressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avanza_stats);

        databaseHelper = new DatabaseHelper(this);

        userId = getSharedPreferences(
                "AvanzaPrefs",
                MODE_PRIVATE
        ).getInt("loggedInUserId", -1);

        if (userId == -1) {

            Intent intent = new Intent(
                    StatsActivity.this,
                    loginActivity.class
            );

            startActivity(intent);
            finish();
            return;
        }

        completionRatePercentage = findViewById(R.id.completionRatePercentage);

        bestStreakNumber = findViewById(R.id.bestStreakNumber);

        totalHabitsNumber = findViewById(R.id.totalHabits);

        completedHabits = findViewById(R.id.completedHabits);

        weeklyProgressRecyclerView = findViewById(R.id.weeklyProgressRecyclerView);

        weeklyProgressRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        weeklyHabitList =
                new ArrayList<>(
                        databaseHelper
                                .getAllHabitsByUser(userId)
                );

        weeklyProgressAdapter =
                new WeeklyProgressAdapter(
                        weeklyHabitList,
                        databaseHelper
                );

        weeklyProgressRecyclerView.setAdapter(
                weeklyProgressAdapter
        );

        updateStats();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Show Stats as selected
        bottomNavigationView.setSelectedItemId(R.id.navStats);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {

                Intent intent = new Intent(
                        StatsActivity.this,
                        AvanzaDashboardActivity.class
                );

                startActivity(intent);
                return true;

            } else if (itemId == R.id.navStats) {

                // Already on Stats
                return true;

            } else if (itemId == R.id.navSettings) {

                Intent intent = new Intent(
                        StatsActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    //Update Stats
    private void updateStats() {

        int completionRate =
                calculateWeeklyCompletionRate();

        int bestStreak = calculateBestStreak();

        int totalHabits =
                calculateTotalHabits();

        int checkInsThisWeek =
                calculateTotalCompletedHabits();

        completionRatePercentage.setText(
                completionRate + "%"
        );

        bestStreakNumber.setText(
                String.valueOf(bestStreak)
        );

        totalHabitsNumber.setText(
                String.valueOf(totalHabits)
        );

        completedHabits.setText(
                String.valueOf(checkInsThisWeek)
        );

        // Refresh Weekly Completion list
        int oldSize = weeklyHabitList.size();

        weeklyHabitList.clear();

        if (oldSize > 0) {
            weeklyProgressAdapter.notifyItemRangeRemoved(
                    0,
                    oldSize
            );
        }

        ArrayList<Habit> updatedHabits =
                new ArrayList<>(
                        databaseHelper.getAllHabitsByUser(userId)
                );

        weeklyHabitList.addAll(updatedHabits);

        if (!updatedHabits.isEmpty()) {
            weeklyProgressAdapter.notifyItemRangeInserted(
                    0,
                    updatedHabits.size()
            );
        }
    }

    //Calculate weekly completion rate
    private int calculateWeeklyCompletionRate(){

        ArrayList<Habit> habits =
                new ArrayList<>(
                        databaseHelper.getAllHabitsByUser(userId)
                );

        if (habits.isEmpty()){
            return 0;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        );

        //Find Monday of current week
        Calendar monday = Calendar.getInstance();

        int currentDay = monday.get(Calendar.DAY_OF_WEEK);
        int daysFromMonday = (currentDay + 5) % 7;

        monday.add(Calendar.DAY_OF_MONTH,
                -daysFromMonday);

        //Find Sunday
        Calendar sunday = (Calendar) monday.clone();

        sunday.add(
                Calendar.DAY_OF_MONTH,
                6
        );

        String startDate = dateFormat.format(monday.getTime());
        String endDate = dateFormat.format(sunday.getTime());

        int completedOccurrences = 0;
        int expectedOccurrences = 0;

        for (Habit habit : habits) {

            int expectedForHabit;

            switch (habit.getFrequency()) {

                case "Daily":
                    expectedForHabit = 7;
                    break;

                case "Weekdays":
                    expectedForHabit = 5;
                    break;

                case "Weekends":
                    expectedForHabit = 2;
                    break;

                case "Weekly":
                    expectedForHabit = 1;
                    break;

                default:
                    expectedForHabit = 0;
                    break;
            }

            expectedOccurrences += expectedForHabit;

            Set<String> completedDates =
                    databaseHelper.getHabitCompletionDatesForRange(
                            habit.getId(),
                            startDate,
                            endDate
                    );

            completedOccurrences += Math.min(
                    completedDates.size(),
                    expectedForHabit
            );
        }

        if (expectedOccurrences == 0) {
            return 0;
        }

        return Math.round(
                (completedOccurrences * 100f) / expectedOccurrences
        );
    }

    private int calculateBestStreak() {

        ArrayList<Habit> habits =
                new ArrayList<>(
                        databaseHelper.getAllHabitsByUser(userId)
                );

        int bestStreak = 0;

        for (Habit habit : habits) {

            ArrayList<String> completionDates =
                    databaseHelper.getAllCompletionDatesForHabit(habit.getId());

            int habitStreak =
                    calculateHabitStreak(
                            habit,
                            completionDates
                    );

            if (habitStreak > bestStreak) {
                bestStreak = habitStreak;
            }
        }

        return bestStreak;
    }

    private int calculateHabitStreak(
            Habit habit,
            ArrayList<String> completionDates) {

        if (completionDates.isEmpty()) {
            return 0;
        }

        if (completionDates.size() == 1) {
            return 1;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        );

        int currentStreak = 1;
        int bestStreak = 1;

        for(int i = 1; i < completionDates.size(); i++) {

            try {

                String previousDateString = completionDates.get(i -1);

                String currentDateString =
                        completionDates.get(i);

                Calendar previousDate =
                        Calendar.getInstance();

                previousDate.setTime(
                        dateFormat.parse(previousDateString)
                );

                Calendar expectedNextDate =
                        getNextScheduledDate(
                                previousDate,
                                habit.getFrequency()
                        );

                String expectedDateString =
                        dateFormat.format(
                                expectedNextDate.getTime()
                        );

                if (currentDateString.equals(expectedDateString)) {

                    currentStreak++;

                } else {

                    currentStreak = 1;
                }

                bestStreak = Math.max(
                        bestStreak,
                        currentStreak
                );
            } catch (Exception e) {
                currentStreak = 1;
            }
        }

        return bestStreak;
    }

    private Calendar getNextScheduledDate(
            Calendar date,
            String frequency) {

        Calendar next =
                (Calendar) date.clone();

        switch (frequency) {

            case "Daily":

                next.add(
                        Calendar.DAY_OF_MONTH,
                        1
                );

                break;

            case "Weekdays":

                do {

                    next.add(
                            Calendar.DAY_OF_MONTH,
                            1
                    );

                } while (
                        next.get(Calendar.DAY_OF_WEEK)
                                == Calendar.SATURDAY
                                ||
                                next.get(Calendar.DAY_OF_WEEK)
                                        == Calendar.SUNDAY
                );

                break;

            case "Weekends":

                do {

                    next.add(
                            Calendar.DAY_OF_MONTH,
                            1
                    );

                } while (
                        next.get(Calendar.DAY_OF_WEEK)
                                != Calendar.SATURDAY
                                &&
                                next.get(Calendar.DAY_OF_WEEK)
                                        != Calendar.SUNDAY
                );

                break;

            case "Weekly":

                next.add(
                        Calendar.DAY_OF_MONTH,
                        7
                );

                break;

            default:

                next.add(
                        Calendar.DAY_OF_MONTH,
                        1
                );

                break;
        }

        return next;
    }

    private int calculateTotalHabits() {

        ArrayList<Habit> habits =
                new ArrayList<>(
                        databaseHelper.getAllHabitsByUser(userId)
                );

        return habits.size();
    }

    private int calculateTotalCompletedHabits() {

        ArrayList<Habit> habits =
                new ArrayList<>(
                        databaseHelper.getAllHabitsByUser(userId)
                );
        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                );

        //Find Monday of the current week
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

        //Find Sunday
        Calendar sunday =
                (Calendar) monday.clone();

        sunday.add(
                Calendar.DAY_OF_MONTH,
                6
        );

        String startDate =
                dateFormat.format(
                        monday.getTime()
                );

        String endDate =
                dateFormat.format(
                        sunday.getTime()
                );

        int habitsCompleted = 0;

        for (Habit habit : habits) {

            Set<String> completedDates =
                    databaseHelper.getHabitCompletionDatesForRange(
                            habit.getId(),
                            startDate,
                            endDate
                    );

            habitsCompleted += completedDates.size();
        }

        return habitsCompleted;
    }

    //Refresh when returning to stats

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null
                && userId != -1
                && completionRatePercentage != null
                && bestStreakNumber != null
                && totalHabitsNumber != null
                && completedHabits != null) {

            updateStats();
        }
    }
}
