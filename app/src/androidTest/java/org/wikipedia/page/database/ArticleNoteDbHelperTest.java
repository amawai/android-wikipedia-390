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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
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
        note = dbHelper.createNote(article, NOTE_CONTENT);
        article.setNote(note);
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
        article.setScroll(UPDATED_SCROLL_STATE);
        dbHelper.updateScrollState(article);
        int updatedScroll = dbHelper.getScrollOfArticle(article);
        assertEquals(UPDATED_SCROLL_STATE, updatedScroll);
    }

    @Test
    public void shouldRetrieveNoteAssociatedToArticle() {
        Note addedNote = dbHelper.getNotesFromArticle(article);
        assertEquals(NOTE_CONTENT, addedNote.getNoteContent());
    }

    @Test
    public void shouldUpdateNote() {
        note.setNoteContent(UPDATED_NOTE_CONTENT);
        dbHelper.updateArticleNote(article);
        Note updatedNote = dbHelper.getNotesFromArticle(article);
        assertEquals(UPDATED_NOTE_CONTENT, updatedNote.getNoteContent());
    }

    @Test
    public void shouldOnlyRetrieveNotesRelatedToSpecificArticle() {
        Article otherArticle= dbHelper.createArticle("Not apple", 0);
        dbHelper.createNote(otherArticle, "desc1");
        //Retrieve and store notes based on article
        Note articleNote = dbHelper.getNotesFromArticle(article);
        Note otherArticleNote = dbHelper.getNotesFromArticle(otherArticle);
        //Ensure that the retrived notes are only associated to one article
        assertNotNull(articleNote);
        assertNotSame(articleNote, otherArticleNote);
    }

    @Test
    public void shouldDeleteNoteAssociatedToArticle(){
        dbHelper.deleteArticleNote(article);
        Note articleNote = dbHelper.getNotesFromArticle(article);
        //Ensure that the note associated to the article is gone
        assertNull(articleNote);
    }

    @After
    public void closeDb(){
        //Close and delete the database that was used for testing
        database.close();
        WikipediaApp.getInstance().deleteDatabase(database.getDatabaseName());
    }

}
