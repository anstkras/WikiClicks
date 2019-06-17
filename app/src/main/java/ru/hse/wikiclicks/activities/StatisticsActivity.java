package ru.hse.wikiclicks.activities;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.activities.adapters.StatisticsListAdapter;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

/** Activity for displaying statistics of previously won games. */
public class StatisticsActivity extends AppCompatActivity {

    /** Creates the activity for displaying statistics, displays steps statistics by default. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Fragment settingsFragment = new StepsFragment(); // Show steps stats as default

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            transaction.add(R.id.fragment_container, settingsFragment);
        }
        transaction.commit();

        setUpShowTimesButton();
        setUpShowStepsButton();
    }

    private void setUpShowTimesButton() {
        final Button showTimesButton = findViewById(R.id.show_times_button);
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
        final Button showStepsButton = findViewById(R.id.show_steps_button);
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

    /** Fragment that displays statistics for time mode games. */
    public static class TimeFragment extends StatisticsFragment {
        @Override
        int getFragmentLayout() {
            return R.layout.time_games_fragment;
        }

        @Override
        int getRecyclerViewId() {
            return R.id.recycleview_statistics_time;
        }

        @Override
        LiveData<List<GameStats>> gameStatsToObserve(GameStatsViewModel gameStatsViewModel) {
            return gameStatsViewModel.getTimeGames();
        }
    }

    /** Fragment that displays statistics for steps mode games. */
    public static class StepsFragment extends StatisticsFragment {

        @Override
        int getFragmentLayout() {
            return R.layout.steps_games_fragment;
        }

        @Override
        int getRecyclerViewId() {
            return R.id.recyclerview_statistics_steps;
        }

        @Override
        LiveData<List<GameStats>> gameStatsToObserve(GameStatsViewModel gameStatsViewModel) {
            return gameStatsViewModel.getStepsGames();
        }
    }

    static abstract class StatisticsFragment extends Fragment {
        /** Creates the fragment stats. */
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        /** Initializes the fragment's view and shows the available statistics. */
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(getFragmentLayout(),
                    container, false);

            GameStatsViewModel gameStatsViewModel = ViewModelProviders.of(this).get(GameStatsViewModel.class);

            final RecyclerView recyclerView = view.findViewById(getRecyclerViewId());
            final List<GameStats> gameStats = new ArrayList<>();
            final StatisticsListAdapter adapter = new StatisticsListAdapter(this.getActivity(), gameStats);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

            gameStatsToObserve(gameStatsViewModel).observe(this, new Observer<List<GameStats>>() {
                /** Modifies the statistics when new games have been won. */
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

        abstract @LayoutRes int getFragmentLayout();
        abstract @IdRes int getRecyclerViewId();
        abstract LiveData<List<GameStats>> gameStatsToObserve(GameStatsViewModel gameStatsViewModel);
    }
}
