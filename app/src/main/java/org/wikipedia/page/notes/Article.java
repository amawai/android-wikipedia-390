package org.wikipedia.page.notes;

import android.support.annotation.NonNull;

import org.wikipedia.page.notes.database.ArticleTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amawai on 27/03/18.
 */

public class Article {
    private long id;
    @NonNull private String title;
    private int scrollPosition;

    private List<Note> notes = new ArrayList<>();

    public static final ArticleTable DATABASE_TABLE = new ArticleTable();

    public Article(String title, int scrollPosition) {
        this.title = title;
        this.scrollPosition = scrollPosition;

    }

    public String getArticleTitle() {
        return this.title;
    }

    public int getScrollPosition() {
        return this.scrollPosition;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
