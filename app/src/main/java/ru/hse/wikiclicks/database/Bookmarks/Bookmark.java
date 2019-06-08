package ru.hse.wikiclicks.database.Bookmarks;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** Class that represents Bookmark entity in Games database's bookmarks table. */
@Entity(tableName = "bookmarks_table",
        indices = {@Index(value = "url",
                unique = true)})
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "url")
    private String url;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    /** Creates a Bookmark for the given url with the given display name. */
    public Bookmark(@NonNull String url, @NonNull String name) {
        this.url = url;
        this.name = name;
    }

    /** Returns the bookmark's id. */
    public int getId() {
        return id;
    }

    /** Returns the bookmark's url. */
    @NonNull
    public String getUrl() {
        return url;
    }

    /** Sets the bookmark's id to equal the given id. */
    public void setId(int id) {
        this.id = id;
    }

    /** Returns the bookmark's name. */
    @NonNull
    public String getName() {
        return name;
    }
}
