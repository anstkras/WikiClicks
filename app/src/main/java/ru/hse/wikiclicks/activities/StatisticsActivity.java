package ru.hse.wikiclicks.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

public class StatisticsActivity extends AppCompatActivity {
    private GameStatsViewModel gameStatsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameStatsViewModel = ViewModelProviders.of(this).get(GameStatsViewModel.class);
        setContentView(R.layout.activity_statistics);

        final ListView listview = findViewById(R.id.listview_statistics_time);

        final ListView listView2 = findViewById(R.id.listview_statistics_steps);

        final ArrayAdapter<Long> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        final ArrayAdapter<Long> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        gameStatsViewModel.getTimeGames().observe(this, new Observer<List<GameStats>>() {
            @Override
            public void onChanged(@Nullable final List<GameStats> games) {
                adapter.clear();
                if (games == null) {
                    return;
                }
                List<Long> times = new ArrayList<>();
                for (GameStats game : games) {
                    times.add(game.getValue());
                }
                adapter.addAll(times);
            }
        });

        gameStatsViewModel.getStepsGames().observe(this, new Observer<List<GameStats>>() {
            @Override
            public void onChanged(@Nullable final List<GameStats> games) {
                adapter2.clear();
                if (games == null) {
                    return;
                }
                List<Long> steps = new ArrayList<>();
                for (GameStats game : games) {
                    steps.add(game.getValue());
                }
                adapter2.addAll(steps);
            }
        });

        listview.setAdapter(adapter);

        listView2.setAdapter(adapter2);
    }
}
