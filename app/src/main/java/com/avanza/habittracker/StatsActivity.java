package com.avanza.habittracker;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
public class StatsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avanza_stats);

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
}
