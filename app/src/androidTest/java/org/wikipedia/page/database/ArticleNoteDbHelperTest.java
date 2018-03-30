package org.wikipedia.page.database;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wikipedia.WikipediaApp;
import org.wikipedia.database.Database;
import org.wikipedia.page.notes.Article;
import org.wikipedia.page.notes.Note;
import org.wikipedia.page.notes.database.ArticleNoteDbHelper;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;


/**
 * Created by amawai on 29/03/18.
 */

public class ArticleNoteDbHelperTest {
    Context mContext;
    Database database;
    ArticleNoteDbHelper dbHelper;

    Note note;
    Article article;
    String ARTICLE_TITLE = "Apple";
    int UPDATED_SCROLL_STATE = 500;

    String NOTE_TITLE = "Note title";
    String NOTE_CONTENT = "Note content";


    String UPDATED_NOTE_TITLE = "UPDATE title";
    String UPDATED_NOTE_CONTENT = "UPDATED content";

    @Before
    public void setUp(){
        mContext = getInstrumentation().getContext();
        database = WikipediaApp.getInstance().getDatabase();
        dbHelper = ArticleNoteDbHelper.instance();

        //Most of the following tests use the article and note that has been created and added to the db here
        article = dbHelper.createArticle(ARTICLE_TITLE, 0);
        note = dbHelper.addNote(article, NOTE_TITLE, NOTE_CONTENT, 0);
    }

    @Test
    public void shouldRetrieveCreateArticle() {
        List<Article> articles = dbHelper.getAllArticles();
        Article createdArticle = articles.get(0);
        assertEquals(articles.size(), 1);
        assertEquals(ARTICLE_TITLE, createdArticle.getArticleTitle());
        assertEquals(0, createdArticle.getScrollPosition());
    }

    @Test
    public void shouldNotCreateArticleIfArticleExistsAlready() {
        //Makes sure that createArticle checks if the article has already been added
        assertNull(dbHelper.createArticle(ARTICLE_TITLE,0));
    }

    @Test
    public void shouldUpdateScrollState(){
        dbHelper.updateScrollState(article, UPDATED_SCROLL_STATE);
        int updatedScroll = dbHelper.getScrollOfArticle(article);
        assertEquals(UPDATED_SCROLL_STATE, updatedScroll);
    }

    @Test
    public void shouldRetrieveNoteAssociatedToArticle() {
        List<Note> notes= dbHelper.getNotesFromArticle(article);
        Note addedNote = notes.get(0);
        assertEquals(NOTE_TITLE, addedNote.getNoteTitle());
        assertEquals(NOTE_CONTENT, addedNote.getNoteContent());
        assertEquals(0, addedNote.getScrollPosition());
    }

    @Test
    public void shouldAddMultipleNotesToArticle() {
        List<Note> notes = new ArrayList<>();
        Note note1 = new Note(article.getId(), "Note1", "Note1 desc", 0);
        Note note2 = new Note(article.getId(), "Note2", "Note2 desc", 0);
        notes.add(note1);
        notes.add(note2);
        dbHelper.addNotes(article, notes);
        List<Note> notesFromDb = dbHelper.getNotesFromArticle(article);
        //The size is 3 because a note is added in the setup
        assertEquals(notesFromDb.size(), 3);
    }

    @Test
    public void shouldUpdateNote() {
        note.setNoteTitle(UPDATED_NOTE_TITLE);
        note.setNoteContent(UPDATED_NOTE_CONTENT);
        note.setScrollPosition(UPDATED_SCROLL_STATE);
        dbHelper.updateNote(note);
        List<Note> notes = dbHelper.getNotesFromArticle(article);
        Note updatedNote = notes.get(0);
        assertEquals(UPDATED_NOTE_TITLE, updatedNote.getNoteTitle());
        assertEquals(UPDATED_NOTE_CONTENT, updatedNote.getNoteContent());
        assertEquals(UPDATED_SCROLL_STATE, updatedNote.getScrollPosition());
    }

    @Test
    public void shouldOnlyRetrieveNotesRelatedToSpecificArticle() {
        Article otherArticle= dbHelper.createArticle("Not apple", 0);
        dbHelper.addNote(otherArticle, "note1", "desc1" ,0);
        dbHelper.addNote(otherArticle, "note2", "desc2", 0);
        //Retrieve and store notes based on article
        List<Note> articleNotes = dbHelper.getNotesFromArticle(article);
        List<Note> otherArticleNotes = dbHelper.getNotesFromArticle(otherArticle);
        //Ensure that the retrived notes are only associated to one article
        assertEquals(1, articleNotes.size());
        assertEquals(2, otherArticleNotes.size());
    }

    @Test
    public void shouldDeleteNoteAssociatedToArticle(){
        dbHelper.deleteNote(note);
        List <Note> notes = dbHelper.getNotesFromArticle(article);
        //Ensure that the note associated to the article is gone
        assertEquals(0, notes.size());
    }

    @After
    public void closeDb(){
        //Close and delete the database that was used for testing
        database.close();
        WikipediaApp.getInstance().deleteDatabase(database.getDatabaseName());
    }

}
