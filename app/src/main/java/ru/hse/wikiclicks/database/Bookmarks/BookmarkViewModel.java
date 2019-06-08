package ru.hse.wikiclicks.database.Bookmarks;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/** View model for a bookmark entity from the database. */
public class BookmarkViewModel extends AndroidViewModel {

    private BookmarkRepository repository;

    private LiveData<List<Bookmark>> bookmarks;

    /** Creates the BookmarkViewModel for an Application. */
    public BookmarkViewModel(@NonNull Application application) {
        super(application);
        repository = new BookmarkRepository(application);
        bookmarks = repository.getBookmarks();
    }

    /** Returns a list of the existing bookmarks. */
    public LiveData<List<Bookmark>> getBookmarks() {
        return bookmarks;
    }

    /** Inserts a new Bookmark, will do nothing if it already exists. */
    public void insert(Bookmark bookmark) {
        repository.insert(bookmark);
    }

    /** Deletes a Bookmark, will do nothing if it does not already exist. */
    public void delete(Bookmark bookmark) {
        repository.delete(bookmark);
    }
}
