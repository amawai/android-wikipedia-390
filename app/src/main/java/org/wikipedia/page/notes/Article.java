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
    private Note note;

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

    public Note getNote() {
        return this.note;
    }

    public void setNote(Note notes) {
        this.note = notes;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setScroll(int scroll) {
        this.scrollPosition = scroll;
    }
}
