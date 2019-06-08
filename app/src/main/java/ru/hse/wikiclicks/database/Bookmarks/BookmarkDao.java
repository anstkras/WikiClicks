package ru.hse.wikiclicks.database.Bookmarks;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/** Interface that represents Room Dao for bookmarks table. */
@Dao
public interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Bookmark bookmark);

    @Query("SElECT * from bookmarks_table ORDER by id DESC")
    LiveData<List<Bookmark>> getBookmarks();

    @Delete
    void deleteBookmarks(Bookmark... bookmarks);
}
