package com.avanza.habittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.widget.PopupMenu;
import android.widget.TextView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import java.util.Calendar;

import com.avanza.habittracker.database.DatabaseHelper;
import com.avanza.habittracker.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    //Class-Level Variables
    private TextView nameText;
    private TextView profileInitial;
    private MaterialButton btnEdit;
    private MaterialButton reminderTimeDropDown;
    private MaterialButton btnSignOut;
    private SwitchMaterial notificationSwitch;
    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper databaseHelper;
    private int userId;
    private int reminderHour;
    private int reminderMinute;

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;

    public static final String CHANNEL_ID = "avanza_habit_reminders";
    private SharedPreferences preferences;


    //On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avanza_settings);

        //Database
        databaseHelper = new DatabaseHelper(this);

        //Logged-in user
        userId = getSharedPreferences(
                "AvanzaPrefs",
                MODE_PRIVATE
        ).getInt("loggedInUserId", -1);

        //Gets logged-in user from SQLite and Updates:
        //nameText
        //profileInitial

        if (userId == -1) {
            // Return to login
            return;
        }

        //Connect XML Views
        nameText = findViewById(R.id.nameText);

        profileInitial = findViewById(R.id.profileInitial);

        btnEdit = findViewById(R.id.btnEdit);

        notificationSwitch = findViewById(R.id.notificationSwitch);

        reminderTimeDropDown = findViewById(R.id.reminderTimeDropDown);

        btnSignOut = findViewById(R.id.btnSignOut);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Create a notification channel
        createNotificationChannel();

        // Load user profile
        loadUserProfile();
        loadNotificationSettings();
        updateReminderTimeText();

        //Notification switch listener
        notificationSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    saveNotificationEnabled(isChecked);

                    if (isChecked) {

                        requestNotificationPermission();

                        scheduleReminder();
                    } else {

                        cancelReminder();
                    }
                }
        );

        //Reminder time button
        reminderTimeDropDown.setOnClickListener(v -> {
            showTimePicker();
        });

        //Edit profile
        btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    AccountSettingsActivity.class
            );

            startActivity(intent);
        });

        //Sign out Button
        btnSignOut.setOnClickListener(v -> {

            signOut();
        });

        // Bottom navigation
        setupBottomNavigation();


        // Notification Switch Enabled
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            saveNotificationEnabled(isChecked);

        });

    }

    //private void loadUserProfile()
    private void loadUserProfile(){

        //Obtains userId from the database
        User userProfile = databaseHelper.getUserById(userId);

        //Profile card name
        if (userProfile != null) {
            String userName = userProfile.getName();

            nameText.setText(userName);

            //Profile picture initial
            if (userName != null && !userName.isEmpty()) {

                String initial = userName.substring(0, 1).toUpperCase();

                profileInitial.setText(initial);
            }

        }

    }

    //private void loadNotificationSettings()
    private void loadNotificationSettings() {
        preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        // Load saved settings
        String savedReminderTime =
                preferences.getString("reminderTime", "12:00pm");

        boolean notificationsEnabled =
                preferences.getBoolean("notificationsEnabled", false);

        reminderTimeDropDown.setText(savedReminderTime);
        notificationSwitch.setChecked(notificationsEnabled);
    }

    //private void saveNotificationEnabled(boolean enabled)
    private void saveNotificationEnabled(boolean enabled) {

        preferences.edit()
                .putBoolean("notificationsEnabled", enabled)
                .apply();

    }

    //private void savedReminderTime(int hour, int minutes)
    private void savedReminderTime(int hour, int minutes) {

        preferences.edit()
                .putInt("reminderHour", hour)
                .putInt("reminderMinute", minutes)
                .apply();

    }

    //private void showTimePicker()
    private void showTimePicker() {

            PopupMenu popupMenu =
                    new PopupMenu(SettingsActivity.this, reminderTimeDropDown);

            popupMenu.getMenu().add("12:00am");
            popupMenu.getMenu().add("1:00am");
            popupMenu.getMenu().add("2:00am");
            popupMenu.getMenu().add("3:00am");
            popupMenu.getMenu().add("4:00am");
            popupMenu.getMenu().add("5:00am");
            popupMenu.getMenu().add("6:00am");
            popupMenu.getMenu().add("7:00am");
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
            popupMenu.getMenu().add("10:00pm");
            popupMenu.getMenu().add("11:00pm");

            popupMenu.setOnMenuItemClickListener(item -> {

                String selectedTime = item.getTitle().toString();

                reminderTimeDropDown.setText(selectedTime);

                boolean isPM =
                        selectedTime.endsWith("pm");

                String timeWithoutPeriod =
                        selectedTime
                                .replace("am", "")
                                .replace("pm", "");

                String[] timeParts =
                        timeWithoutPeriod.split(":");

                int hour = Integer.parseInt(timeParts[0]);

                int minutes = Integer.parseInt(timeParts[1]);

                // Convert 12-hour time to 24-hour time
                if (hour == 12) {
                    hour = 0;
                }

                if (isPM) {
                    hour += 12;
                }

                savedReminderTime(
                        hour,
                        minutes
                );

                if (notificationSwitch.isChecked()) {
                    scheduleReminder();
                }

                return true;
            });

            popupMenu.show();

    }

    //private void updateReminderTimeText()
    private void updateReminderTimeText() {

        int hour = preferences.getInt("reminderHour", 12);

        int minutes = preferences.getInt("reminderMinutes", 0);

        String period = hour >= 12 ? "pm" : "am";

        int displayHour = hour % 12;

        if (displayHour == 0) {
            displayHour = 12;
        }

        String formattedTime = String.format(Locale.getDefault(),
                "%d:%02d%s",
                displayHour,
                minutes,
                period
        );

        reminderTimeDropDown.setText(formattedTime);

    }

    private void createNotificationChannel() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String channelName = "Habit Reminders";

            String channelDescription = "Daily reminders to check in on your habits";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    channelName,
                    importance
            );

            channel.setDescription(channelDescription);

            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);

        }

    }

    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.POST_NOTIFICATIONS
                        },
                        NOTIFICATION_PERMISSION_REQUEST_CODE
                );
            }
        }
    }

    //private void scheduleReminder()
    private void scheduleReminder () {

        int hour =
                preferences.getInt("reminderHour", 12);

        int minute =
                preferences.getInt("reminderMinute", 0);

        Calendar calendar =
                Calendar.getInstance();

        calendar.set(
                Calendar.HOUR_OF_DAY,
                hour
        );

        calendar.set(
                Calendar.MINUTE,
                minute
        );

        calendar.set(
                Calendar.SECOND,
                0
        );

        calendar.set(
                Calendar.MILLISECOND,
                0
        );

        // If today's reminder time has already passed,
        // schedule the first reminder for tomorrow
        if (calendar.getTimeInMillis()
                <= System.currentTimeMillis()) {

            calendar.add(
                    Calendar.DAY_OF_YEAR,
                    1
            );
        }

        Intent intent =
                new Intent(
                        SettingsActivity.this,
                        ReminderReceiver.class
                );

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        SettingsActivity.this,
                        userId,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(
                        Context.ALARM_SERVICE
                );

        if (alarmManager != null) {

            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }

    //private void cancelReminder()
    private void cancelReminder () {

        Intent intent =
                new Intent(
                        SettingsActivity.this,
                        ReminderReceiver.class
                );

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        SettingsActivity.this,
                        userId,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        AlarmManager alarmManager =
                (AlarmManager) getSystemService(
                        Context.ALARM_SERVICE
                );

        if (alarmManager != null) {

            alarmManager.cancel(
                    pendingIntent
            );

            pendingIntent.cancel();
        }
    }

    private void signOut() {
        Intent intent = new Intent(
                SettingsActivity.this,
                loginActivity.class
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }

    private void setupBottomNavigation(){

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

            else // Already on Settings screen
                if (itemId == R.id.navStats) {

                Intent intent =
                        new Intent(SettingsActivity.this,
                                StatsActivity.class);

                startActivity(intent);
                finish();

                return true;
            }

            else return itemId == R.id.navSettings;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && userId != -1) {
            loadUserProfile();
        }
    }
}
