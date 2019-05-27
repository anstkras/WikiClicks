package ru.hse.wikiclicks.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Fragment settingsFragment = new TimeFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            transaction.add(R.id.fragment_container, settingsFragment);
        }
        transaction.commit();

        setUpShowTimesButton();
        setUpShowStepsButton();
    }

    private void setUpShowTimesButton() {
        Button showTimesButton = findViewById(R.id.show_times_button);
        showTimesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment timesFragment = new TimeFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, timesFragment);
                transaction.commit();
            }
        });
    }

    private void setUpShowStepsButton() {
        Button showStepsButton = findViewById(R.id.show_steps_button);
        showStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment stepsFragment = new StepsFragment();

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, stepsFragment);
                transaction.commit();
            }
        });
    }

    public static class TimeFragment extends Fragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.time_games_fragment,
                    container, false);

            GameStatsViewModel gameStatsViewModel = ViewModelProviders.of(this.getActivity()).get(GameStatsViewModel.class);


            final ListView listview = view.findViewById(R.id.listview_statistics_time);

            final ArrayAdapter<Long> adapter = new ArrayAdapter<>(this.getActivity(),
                    android.R.layout.simple_list_item_1);


            gameStatsViewModel.getTimeGames().observe(this.getActivity(), new Observer<List<GameStats>>() {
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

            listview.setAdapter(adapter);
            return view;
        }
    }

    public static class StepsFragment extends Fragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.steps_games_fragment,
                    container, false);

            GameStatsViewModel gameStatsViewModel = ViewModelProviders.of(this).get(GameStatsViewModel.class);



            final ListView listView2 = view.findViewById(R.id.listview_statistics_steps);


            final ArrayAdapter<Long> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1);


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
            listView2.setAdapter(adapter2);
            return view;
        }
    }
}
