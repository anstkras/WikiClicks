package ru.hse.wikiclicks.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.database.GameStats.GameStats;

public class StatisticsListAdapter extends RecyclerView.Adapter<StatisticsListAdapter.ViewHolder> {

    private List<GameStats> gameStats;
    private LayoutInflater inflater;

    public StatisticsListAdapter(Context context, List<GameStats> data) {
        this.inflater = LayoutInflater.from(context);
        this.gameStats = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.statistics_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameStats game = gameStats.get(position);
        holder.fromTextView.setText(game.getFrom());
        holder.toTextView.setText(game.getTo());
        if (game.isTime()){
            holder.valueTextView.setText(getTimeFromMillis(game.getValue()));
        } else {
            holder.valueTextView.setText(String.valueOf(game.getValue()));
        }
    }

    @Override
    public int getItemCount() {
        return gameStats.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fromTextView;
        private TextView toTextView;
        private TextView valueTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            fromTextView = itemView.findViewById(R.id.from_textView);
            toTextView = itemView.findViewById(R.id.to_textView);
            valueTextView = itemView.findViewById(R.id.value_textView);
        }
    }

    private String getTimeFromMillis(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}