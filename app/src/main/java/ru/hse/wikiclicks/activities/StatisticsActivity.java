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
import ru.hse.wikiclicks.database.GamesViewModel;
import ru.hse.wikiclicks.database.TimeModeGame;

public class StatisticsActivity extends AppCompatActivity {
    private GamesViewModel gamesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gamesViewModel = ViewModelProviders.of(this).get(GamesViewModel.class);
        setContentView(R.layout.activity_statistics);

        final ListView listview = findViewById(R.id.listview_statistics);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);

        gamesViewModel.getAllGames().observe(this, new Observer<List<TimeModeGame>>() {
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
        listview.setAdapter(adapter);
    }
}
