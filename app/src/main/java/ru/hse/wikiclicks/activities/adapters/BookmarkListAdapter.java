package ru.hse.wikiclicks.activities.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.database.Bookmarks.Bookmark;
import ru.hse.wikiclicks.database.Bookmarks.BookmarkViewModel;

/** Class that represents an adapter for recycle view of bookmarks */
public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.ViewHolder> {

    private final List<Bookmark> bookmarks;
    private final LayoutInflater inflater;
    private final Context context;
    private final BookmarkViewModel bookmarkViewModel;

    public BookmarkListAdapter(Context context, BookmarkViewModel bookmarkViewModel, List<Bookmark> data) {
        this.inflater = LayoutInflater.from(context);
        this.bookmarks = data;
        this.context = context;
        this.bookmarkViewModel = bookmarkViewModel;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bookmark_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Bookmark bookmark = bookmarks.get(position);
        holder.bookmarkTextView.setText(bookmark.getName());
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookmarkTextView;

        private ViewHolder(View itemView) {
            super(itemView);
            bookmarkTextView = itemView.findViewById(R.id.bookmark_text_view);
            bookmarkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bookmark bookmark = bookmarks.get(getAdapterPosition());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.getUrl()));
                    context.startActivity(browserIntent);
                }
            });

            final Button deleteButton = itemView.findViewById(R.id.bookmark_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you sure you want to delete this bookmark?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Bookmark bookmark = bookmarks.remove(getAdapterPosition());
                                    notifyDataSetChanged();
                                    bookmarkViewModel.delete(bookmark);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .create()
                            .show();
                }
            });
        }
    }
}