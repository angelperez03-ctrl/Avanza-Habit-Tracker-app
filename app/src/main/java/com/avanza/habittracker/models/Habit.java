package com.avanza.habittracker.models;

import android.database.Cursor;
import com.avanza.habittracker.models.User;
import android.database.sqlite.SQLiteDatabase;

public class Habit {

    private int id;
    private String name;
    private String description;
    private String frequency;
    private int streak;

    public Habit(int id, String name, String description, String frequency, int streak) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.streak = streak;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getFrequency(){
        return frequency;
    }

    public int getStreak(){
        return streak;
    }

}
