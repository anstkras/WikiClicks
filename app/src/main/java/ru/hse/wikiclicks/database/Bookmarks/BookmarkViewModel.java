package ru.hse.wikiclicks.database.Bookmarks;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BookmarkViewModel extends AndroidViewModel {

    private BookmarkRepository repository;

    private LiveData<List<Bookmark>> bookmarks;


    public BookmarkViewModel(@NonNull Application application) {
        super(application);
        repository = new BookmarkRepository(application);
        bookmarks = repository.getBookmarks();
    }

    public LiveData<List<Bookmark>> getBookmarks() {
        return bookmarks;
    }

    public void insert(Bookmark bookmark) {
        repository.insert(bookmark);
    }

    public void delete(Bookmark bookmark) {
        repository.delete(bookmark);
    }
}
