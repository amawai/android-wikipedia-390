package org.wikipedia.page.notes;

import org.wikipedia.page.notes.database.ArticleNoteTable;

/**
 * Created by amawai on 27/03/18.
 */

public class Note {
    private long id;

    private String noteTitle;
    private String noteContent;
    private int scrollPosition;

    public static final ArticleNoteTable DATABASE_TABLE = new ArticleNoteTable();

    public Note(String noteTitle, String noteContent, int scrollPosition) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.scrollPosition = scrollPosition;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getNoteId() {
        return this.id;
    }
}
