package ru.hse.wikiclicks.database.Bookmarks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks_table")
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="url")
    private String url;

    @ColumnInfo(name="name")
    private String name;

    public Bookmark(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
