package ru.hse.wikiclicks.database.Bookmarks;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** Class that represents Bookmark entity in Games database's bookmarks table */
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

    public Bookmark(@NonNull String url, @NonNull String name) {
        this.url = url;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
