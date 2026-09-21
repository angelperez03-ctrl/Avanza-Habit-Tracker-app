package com.avanza.habittracker.models;

import android.database.Cursor;
import com.avanza.habittracker.models.User;
import android.database.sqlite.SQLiteDatabase;

public class User {

    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private String passwordSalt;

    public User(int id, String name, String email, String passwordHash, String passwordSalt) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPasswordHash(){
        return passwordHash;
    }

    public String getPasswordSalt(){
        return passwordSalt;
    }

}
