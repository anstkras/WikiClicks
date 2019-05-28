package ru.hse.wikiclicks.database.Bookmarks;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BookmarkDao {
    @Insert
    void insert(Bookmark bookmark);

    @Query("SElECT * from bookmarks_table ORDER by id DESC")
    LiveData<List<Bookmark>> getBookmarks();
}
