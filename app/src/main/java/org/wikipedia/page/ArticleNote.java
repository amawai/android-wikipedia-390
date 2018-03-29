package org.wikipedia.page;

/**
 * Created by mnhn3 on 2018-03-28.
 */

public class ArticleNote {
    private String noteTitle;
    private String noteContent;
    private int position;

    public ArticleNote(){
        this.noteTitle = "";
        this.noteContent = "";
        this.position = 0;
    }

    public ArticleNote(String noteTitle, String noteContent, int position) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.position = position;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public int getPosition() {
        return position;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
