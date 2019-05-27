package ru.hse.wikiclicks.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Fragment settingsFragment = new StepsFragment();

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

            GameStatsViewModel gameStatsViewModel = ViewModelProviders.of(this).get(GameStatsViewModel.class);


            final RecyclerView recyclerView = view.findViewById(R.id.recycleview_statistics_time);

            final List<GameStats> gameStats = new ArrayList<>();
            final StatisticsListAdapter adapter = new StatisticsListAdapter(this.getActivity(), gameStats);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


            gameStatsViewModel.getTimeGames().observe(this, new Observer<List<GameStats>>() {
                @Override
                public void onChanged(@Nullable final List<GameStats> games) {
                    gameStats.clear();
                    if (games == null) {
                        return;
                    }
                    gameStats.addAll(games);
                    adapter.notifyDataSetChanged();
                }
            });

            recyclerView.setAdapter(adapter);
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



            final RecyclerView recyclerView = view.findViewById(R.id.listview_statistics_steps);


            final List<GameStats> gameStats = new ArrayList<>();
            final StatisticsListAdapter adapter = new StatisticsListAdapter(this.getActivity(), gameStats);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


            gameStatsViewModel.getStepsGames().observe(this, new Observer<List<GameStats>>() {
                @Override
                public void onChanged(@Nullable final List<GameStats> games) {
                    gameStats.clear();
                    if (games == null) {
                        return;
                    }
                    gameStats.addAll(games);
                    adapter.notifyDataSetChanged();
                }
            });

            recyclerView.setAdapter(adapter);
            return view;
        }
    }
}
