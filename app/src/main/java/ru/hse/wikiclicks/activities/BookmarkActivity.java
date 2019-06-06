package ru.hse.wikiclicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.activities.adapters.BookmarkListAdapter;
import ru.hse.wikiclicks.database.Bookmarks.Bookmark;
import ru.hse.wikiclicks.database.Bookmarks.BookmarkViewModel;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        BookmarkViewModel bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel.class);

        final RecyclerView recyclerView = findViewById(R.id.bookmarks_recycler_view);

        final List<Bookmark> bookmarks = new ArrayList<>();
        final BookmarkListAdapter adapter = new BookmarkListAdapter(this, bookmarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookmarkViewModel.getBookmarks().observe(this, new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> updatedBookmarks) {
                bookmarks.clear();
                if (updatedBookmarks == null) {
                    return;
                }
                bookmarks.addAll(updatedBookmarks);
                adapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
