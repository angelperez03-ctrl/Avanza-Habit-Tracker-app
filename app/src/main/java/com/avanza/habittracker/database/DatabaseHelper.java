package com.avanza.habittracker.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.avanza.habittracker.models.Habit;
import com.avanza.habittracker.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "avanza.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_USERS = "users";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD_HASH = "password_hash";
    public static final String COLUMN_USER_PASSWORD_SALT = "password_salt";

    public static final String TABLE_HABITS = "habits";

    public static final String COLUMN_HABIT_ID = "habit_id";
    public static final String COLUMN_HABIT_USER_ID = "habit_user_id";
    public static final String COLUMN_HABIT_NAME = "habit_name";
    public static final String COLUMN_HABIT_DESCRIPTION = "habit_description";
    public static final String COLUMN_HABIT_FREQUENCY = "habit_frequency";
    public static final String COLUMN_HABIT_STREAK = "habit_streak";

    public static final String TABLE_HABIT_COMPLETIONS = "habit_completions";

    public static final String COLUMN_HABIT_COMPLETION_ID = "habit_completion_id";
    public static final String COLUMN_HABIT_COMPLETION_HABIT_ID = "habit_completion_habit_id";
    public static final String COLUMN_HABIT_COMPLETION_DATE = "habit_completion_date";
    public static final String COLUMN_HABIT_COMPLETED = "completed";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        String createUsersTable =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_USER_NAME + " TEXT NOT NULL, " +
                        COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE, " +
                        COLUMN_USER_PASSWORD_HASH + " TEXT NOT NULL, " +
                        COLUMN_USER_PASSWORD_SALT + " TEXT NOT NULL " +
                        ")";

        String createHabitsTable =
                "CREATE TABLE " + TABLE_HABITS + " (" +
                        COLUMN_HABIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_HABIT_USER_ID + " INTEGER NOT NULL, " +
                        COLUMN_HABIT_NAME + " TEXT NOT NULL, " +
                        COLUMN_HABIT_DESCRIPTION + " TEXT, " +
                        COLUMN_HABIT_FREQUENCY + " TEXT NOT NULL, " +
                        COLUMN_HABIT_STREAK + " INTEGER DEFAULT 0, " +
                        "FOREIGN KEY (" + COLUMN_HABIT_USER_ID + ") REFERENCES " +
                        TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                        ")";

        String createHabitCompletionsTable =
                "CREATE TABLE " + TABLE_HABIT_COMPLETIONS + " (" +
                        COLUMN_HABIT_COMPLETION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_HABIT_COMPLETION_HABIT_ID + " INTEGER NOT NULL, " +
                        COLUMN_HABIT_COMPLETION_DATE + " TEXT NOT NULL, " +
                        COLUMN_HABIT_COMPLETED + " INTEGER NOT NULL DEFAULT 0," +
                        "FOREIGN KEY (" + COLUMN_HABIT_COMPLETION_HABIT_ID + ") REFERENCES " +
                        TABLE_HABITS + "(" + COLUMN_HABIT_ID + ")" +
                        ")";

        db.execSQL(createUsersTable);
        db.execSQL(createHabitsTable);
        db.execSQL(createHabitCompletionsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABIT_COMPLETIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HABITS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(db);
    }

    public long addUser(String name, String email, String passwordHash, String passwordSalt) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD_HASH, passwordHash);
        values.put(COLUMN_USER_PASSWORD_SALT, passwordSalt);

        long result = db.insert(TABLE_USERS, null, values);

        db.close();

        return result;
    }

    public User getUserById(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        User user = null;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_ID)
            );

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)
            );

            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)
            );

            String passwordHash = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD_HASH)
            );

            String passwordSalt = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD_SALT)
            );

            user = new User(
                    id,
                    name,
                    email,
                    passwordHash,
                    passwordSalt
            );

        }

        cursor.close();

        return user;
    }

    public int updateUserPassword(
            int userId,
            String newPasswordHash,
            String newPasswordSalt) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_PASSWORD_HASH, newPasswordHash);
        values.put(COLUMN_USER_PASSWORD_SALT, newPasswordSalt);

        int rowsAffected = db.update(
                TABLE_USERS,
                values,
                COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );

        db.close();

        return rowsAffected;
    }

    public User getUserByEmail(String email){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                null,
                COLUMN_USER_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );

        User user = null;

        if (cursor.moveToFirst()) {

            int userId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_ID)
            );

            String userName = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)
            );

            String userEmail = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)
            );

            String passwordHash = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD_HASH)
            );

            String passwordSalt = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD_SALT)
            );

            user = new User(
                    userId,
                    userName,
                    userEmail,
                    passwordHash,
                    passwordSalt
            );
        }

        cursor.close();

        return user;
    }

    public long addHabit(int userId, String name, String description, String frequency) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_HABIT_USER_ID, userId);
        values.put(COLUMN_HABIT_NAME, name);
        values.put(COLUMN_HABIT_DESCRIPTION, description);
        values.put(COLUMN_HABIT_FREQUENCY, frequency);
        values.put(COLUMN_HABIT_STREAK, 0);

        long result = db.insert(TABLE_HABITS, null, values);

        db.close();

        return result;
    }

    public List<Habit> getAllHabitsByUser(int userId){

        SQLiteDatabase db = this.getReadableDatabase();

        List<Habit> habits = new ArrayList<>();

        Cursor cursor = db.query(
                TABLE_HABITS,
                null,
                COLUMN_HABIT_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int habitId = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_HABIT_ID)
                );

                String habitName = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_HABIT_NAME)
                );

                String habitDescription = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_HABIT_DESCRIPTION)
                );

                String habitFrequency = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_HABIT_FREQUENCY)
                );

                int habitStreak = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_HABIT_STREAK)
                );

                Habit habit = new Habit(
                        habitId,
                        habitName,
                        habitDescription,
                        habitFrequency,
                        habitStreak
                );

                habits.add(habit);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return habits;
    }

    public int updateHabit(int habitId, String newName, String newDescription, String newFrequency, int newStreak ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_HABIT_NAME, newName);
        values.put(COLUMN_HABIT_DESCRIPTION, newDescription);
        values.put(COLUMN_HABIT_FREQUENCY, newFrequency);
        values.put(COLUMN_HABIT_STREAK, newStreak);

        int rowsAffected = db.update(
                TABLE_HABITS,
                values,
                COLUMN_HABIT_ID + " = ?",
                new String[]{String.valueOf(habitId)}
        );

        db.close();

        return rowsAffected;
    }

    public int deleteHabit(int habitId) {

        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_HABITS,
                COLUMN_HABIT_ID + " = ?",
                new String[]{String.valueOf(habitId)}
        );

        db.close();

        return rowsDeleted;
    }

    public long addHabitCompletion(int habitId, String date, int completed) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_HABIT_COMPLETION_HABIT_ID, habitId);
        values.put(COLUMN_HABIT_COMPLETION_DATE, date);
        values.put(COLUMN_HABIT_COMPLETED, completed);

        long result = db.insert(TABLE_HABIT_COMPLETIONS, null, values);

        db.close();

        return result;
    }

    public Habit getHabitById(int habitId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_HABITS,
                null,
                COLUMN_HABIT_ID + " = ?",
                new String[]{String.valueOf(habitId)},
                null,
                null,
                null
        );

        Habit habit = null;

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_HABIT_ID)
            );

            String habitName = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_HABIT_NAME)
            );

            String habitDescription = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_HABIT_DESCRIPTION)
            );

            String habitFrequency = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_HABIT_FREQUENCY)
            );

            int habitStreak = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_HABIT_STREAK)
            );

            habit = new Habit(
                    id,
                    habitName,
                    habitDescription,
                    habitFrequency,
                    habitStreak
            );
        }
        cursor.close();

        return habit;
    }

    public int getCompletedHabitCountForDate(int userId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query =
                "SELECT COUNT(*) FROM " + TABLE_HABIT_COMPLETIONS +
                " INNER JOIN " + TABLE_HABITS +
                " ON " + TABLE_HABIT_COMPLETIONS + "." +
                COLUMN_HABIT_COMPLETION_HABIT_ID +
                " = " + TABLE_HABITS + "." + COLUMN_HABIT_ID +
                " WHERE " + TABLE_HABITS + "." +
                COLUMN_HABIT_USER_ID + " = ?" +
                " AND " + TABLE_HABIT_COMPLETIONS + "." +
                COLUMN_HABIT_COMPLETION_DATE + " = ?" +
                " AND " + TABLE_HABIT_COMPLETIONS + "." +
                COLUMN_HABIT_COMPLETED + " = 1";

        Cursor cursor = db.rawQuery(
                query,
                new String[]{
                        String.valueOf(userId),
                        date
                }
        );

        int completedCount = 0;

        if (cursor.moveToFirst()) {
            completedCount = cursor.getInt(0);
        }

        cursor.close();

        return completedCount;
    }

    public boolean isHabitCompletedForDate(int habitId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_HABIT_COMPLETIONS,
                new String[]{COLUMN_HABIT_COMPLETION_ID},
                COLUMN_HABIT_COMPLETION_HABIT_ID + " = ? AND " +
                        COLUMN_HABIT_COMPLETION_DATE + " = ? AND " +
                        COLUMN_HABIT_COMPLETED + " = 1",
                new String[]{
                        String.valueOf(habitId),
                        date
                },
                null,
                null,
                null
        );
        boolean completed = cursor.moveToFirst();

        cursor.close();
        
        return completed;
    }
}


