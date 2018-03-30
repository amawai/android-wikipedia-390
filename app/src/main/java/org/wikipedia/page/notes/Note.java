package org.wikipedia.page.notes;

import org.wikipedia.page.notes.database.ArticleNoteTable;

/**
 * Created by amawai on 27/03/18.
 */

public class Note {
    private long id;

    private long articleId;
    private String noteTitle;
    private String noteContent;
    private int scrollPosition;

    public static final ArticleNoteTable DATABASE_TABLE = new ArticleNoteTable();

    public Note(long articleId, String noteTitle, String noteContent, int scrollPosition) {
        this.articleId = articleId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.scrollPosition = scrollPosition;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setArticleId(long id) {
        this.articleId = id;
    }


    public void setNoteTitle(String title) {
        this.noteTitle = title;
    }

    public void setNoteContent(String content) {
        this.noteContent = content;
    }

    public void setScrollPosition(int scroll) {
        this.scrollPosition = scroll;
    }

    public String getNoteTitle() {
        return this.noteTitle;
    }

    public String getNoteContent() {
        return this.noteContent;
    }

    public int getScrollPosition() {
        return this.scrollPosition;
    }

    public long getArticleId() {
        return this.articleId;
    }

    public long getNoteId() {
        return this.id;
    }
}
