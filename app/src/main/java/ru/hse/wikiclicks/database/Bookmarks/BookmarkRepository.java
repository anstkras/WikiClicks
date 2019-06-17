package ru.hse.wikiclicks.database.Bookmarks;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import ru.hse.wikiclicks.database.GamesDatabase;

// Class that provides methods to manage bookmarks table
class BookmarkRepository {

    private final BookmarkDao bookmarkDao;
    private final LiveData<List<Bookmark>> bookmarks;

    BookmarkRepository(Application application) {
        GamesDatabase db = GamesDatabase.getDatabase(application);
        bookmarkDao = db.bookmarkDao();
        bookmarks = bookmarkDao.getBookmarks();
    }

    LiveData<List<Bookmark>> getBookmarks() {
        return bookmarks;
    }

    void insert(Bookmark bookmark) {
        new BookmarkRepository.insertAsyncTask(bookmarkDao).execute(bookmark);
    }

    void delete(Bookmark bookmark) {
        new BookmarkRepository.deleteAsyncTask(bookmarkDao).execute(bookmark);
    }

    private static class insertAsyncTask extends AsyncTask<Bookmark, Void, Void> {

        private final BookmarkDao asyncTaskDao;

        private insertAsyncTask(BookmarkDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Bookmark... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Bookmark, Void, Void> {

        private final BookmarkDao asyncTaskDao;

        private deleteAsyncTask(BookmarkDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Bookmark... params) {
            asyncTaskDao.deleteBookmarks(params);
            return null;
        }
    }
}
