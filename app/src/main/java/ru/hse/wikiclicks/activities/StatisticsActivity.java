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
import ru.hse.wikiclicks.database.StepsMode.StepsModeGame;
import ru.hse.wikiclicks.database.StepsMode.StepsModeGamesViewModel;
import ru.hse.wikiclicks.database.TimeMode.TimeModeGamesViewModel;
import ru.hse.wikiclicks.database.TimeMode.TimeModeGame;

public class StatisticsActivity extends AppCompatActivity {
    private StepsModeGamesViewModel stepsModeGamesViewModel;
    private TimeModeGamesViewModel timeModeGamesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeModeGamesViewModel = ViewModelProviders.of(this).get(TimeModeGamesViewModel.class);
        stepsModeGamesViewModel = ViewModelProviders.of(this).get(StepsModeGamesViewModel.class);
        setContentView(R.layout.activity_statistics);

        final ListView listview = findViewById(R.id.listview_statistics_time);

        final ListView listView2 = findViewById(R.id.listview_statistics_steps);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        timeModeGamesViewModel.getAllGames().observe(this, new Observer<List<TimeModeGame>>() {
            @Override
            public void onChanged(@Nullable final List<TimeModeGame> games) {
                adapter.clear();
                if (games == null) {
                    return;
                }
                List<String> times = new ArrayList<>();
                for (TimeModeGame game : games) {
                    times.add(game.getTime());
                }
                adapter.addAll(times);
            }
        });

        stepsModeGamesViewModel.getAllGames().observe(this, new Observer<List<StepsModeGame>>() {
            @Override
            public void onChanged(@Nullable final List<StepsModeGame> games) {
                adapter2.clear();
                if (games == null) {
                    return;
                }
                List<String> steps = new ArrayList<>();
                for (StepsModeGame game : games) {
                    steps.add(Long.toString(game.getSteps()));
                }
                adapter2.addAll(steps);
            }
        });

        listview.setAdapter(adapter);

        listView2.setAdapter(adapter2);
    }
}
