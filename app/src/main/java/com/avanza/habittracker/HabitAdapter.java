package com.avanza.habittracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avanza.habittracker.models.Habit;

import java.util.ArrayList;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private final ArrayList<Habit> habits;

    public HabitAdapter(ArrayList<Habit> habits) {
        this.habits = habits;
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