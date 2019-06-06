package ru.hse.wikiclicks.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.database.Bookmarks.Bookmark;

public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.ViewHolder> {

    private List<Bookmark> bookmarks;
    private LayoutInflater inflater;
    private Context context;

    public BookmarkListAdapter(Context context, List<Bookmark> data) {
        this.inflater = LayoutInflater.from(context);
        this.bookmarks = data;
        this.context = context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bookmark_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bookmark bookmark = bookmarks.get(position);
        holder.bookmarkTextView.setText(bookmark.getName());
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bookmarkTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bookmarkTextView = itemView.findViewById(R.id.bookmark_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bookmark bookmark = bookmarks.get(getAdapterPosition());
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.getUrl()));
            context.startActivity(browserIntent);
        }
    }
}