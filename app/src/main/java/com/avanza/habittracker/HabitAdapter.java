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
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avanza.habittracker.models.Habit;

import java.util.ArrayList;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private Context context;
    private final ArrayList<Habit> habits;

    private OnHabitCompletedListener completionListener;

    public interface OnHabitCompletedListener {
        void onHabitCompleted();
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

        holder.txtHabitStreak.setText(
                "Streak: " + habit.getStreak() + "d"
        );

        holder.txtFrequency.setText(
                habit.getFrequency()
        );

        String today = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(new Date());

        DatabaseHelper databaseHelper =
                new DatabaseHelper(holder.itemView.getContext());

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

            if (!alreadyCompleted) {

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

                    if (completionListener != null) {
                        completionListener.onHabitCompleted();
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
        }
    }
}