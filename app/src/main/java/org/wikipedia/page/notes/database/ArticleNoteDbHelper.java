package org.wikipedia.page.notes.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import org.wikipedia.WikipediaApp;
import org.wikipedia.database.contract.ArticleContract;
import org.wikipedia.database.contract.ArticleNoteContract;
import org.wikipedia.page.notes.Article;
import org.wikipedia.page.notes.Note;
import org.wikipedia.util.log.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amawai on 28/03/18.
 */

public class ArticleNoteDbHelper {
    private static ArticleNoteDbHelper INSTANCE;

    public static ArticleNoteDbHelper instance() {
        if (INSTANCE == null) {
            INSTANCE = new ArticleNoteDbHelper();
        }
        return INSTANCE;
    }

    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(ArticleContract.TABLE, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                Article article = Article.DATABASE_TABLE.fromCursor(cursor);
                articleList.add(article);
            }
        }
        for (Article list : articleList) {
            Log.d("ARTICLE_HELPER", "title: " + list.getArticleTitle() + " scroll : " + list.getScrollPosition());
        }
        return articleList;
    }

    public int getScrollOfArticle(Article article) {
        SQLiteDatabase db = getReadableDatabase();
        int scrollPosition = 0;
        try (Cursor cursor = db.query(ArticleContract.TABLE, null, ArticleContract.Col.ARTICLE_TITLE.getName() + " = ?",
                new String[]{article.getArticleTitle()},
                null, null, null)) {
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                scrollPosition = Article.DATABASE_TABLE.fromCursor(cursor).getScrollPosition();
            }
        }
        return scrollPosition;
    }

    @NonNull
    public Article createArticle(@NonNull String title, @NonNull int scroll) {
        SQLiteDatabase db = getWritableDatabase();
        return createArticle(db, title, scroll);
    }

    @NonNull
    public Article createArticle(@NonNull SQLiteDatabase db, @NonNull String title, @NonNull int scroll) {
            db.beginTransaction();
            try {
                if (!articleExistsInDb(getReadableDatabase(), title)) {
                    Article createdArticle = new Article(title, scroll);
                    long id = db.insertOrThrow(ArticleContract.TABLE, null,
                            Article.DATABASE_TABLE.toContentValues(createdArticle));
                    db.setTransactionSuccessful();
                    createdArticle.setId(id);
                    return createdArticle;
                }
            } finally {
                db.endTransaction();
            }
        return null;
    }

    public Article getArticleByTitle(String title) {
        Article article = null;
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(ArticleContract.TABLE, null,
                ArticleContract.Col.ARTICLE_TITLE.getName() + " = ?", new String[]{title},
                null, null,null)) {
            if (cursor.moveToFirst()) {
                article = Article.DATABASE_TABLE.fromCursor(cursor);
            }
        }
        return article;
    }

    public void updateScrollState(@NonNull Article article) {
        SQLiteDatabase db = getWritableDatabase();
        updateScrollState(db, article);
    }

    public void updateScrollState(@NonNull SQLiteDatabase db, @NonNull Article article) {
        db.beginTransaction();
        try {
            updateScrollInDb(db, article);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public Note createNote(@NonNull Article article, String noteDescription) {
        Note newNote = new Note(article.getId(), noteDescription);
        return createNote(article, newNote);
    }

    public Note createNote(@NonNull Article article, @NonNull Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            long id = db.insertOrThrow(ArticleNoteContract.TABLE, null,
                    Note.DATABASE_TABLE.toContentValues(note));
            db.setTransactionSuccessful();
            note.setId(id);
            return note;
        } finally {
            db.endTransaction();
        }
    }


    public void updateArticleNote(@NonNull Article article) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            updateNoteInDb(db, article.getNote());
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    public void deleteArticleNote(@NonNull Article article) {
        deleteNoteFromDb(getReadableDatabase(), article.getNote());
    }

    public Note getNotesFromArticle(@NonNull Article article) {
        SQLiteDatabase db = getReadableDatabase();
        return getNotesFromArticle(db, article);
    }


    public Note getNotesFromArticle(@NonNull SQLiteDatabase db, @NonNull Article article) {
        Note note = null;
        try (Cursor cursor = db.query(ArticleNoteContract.TABLE, null,
                ArticleNoteContract.Col.ARTICLE_ID.getName() + " = ?", new String[]{Long.toString(article.getId())},
                null, null,null)) {
            if (cursor.moveToFirst()) {
                note = Note.DATABASE_TABLE.fromCursor(cursor);
            }
        }
        return note;
    }

    private void updateScrollInDb(SQLiteDatabase db, @NonNull Article article) {
        int result = db.update(ArticleContract.TABLE, article.DATABASE_TABLE.toContentValues(article),
                ArticleContract.Col.ID.getName() + " = ?", new String[]{Long.toString(article.getId())});
        if (result != 1) {
            L.w("Failed to update scroll position for article " + article.getArticleTitle() + " at scroll " + article.getScrollPosition());
        }
    }

    private void updateNoteInDb(SQLiteDatabase db, @NonNull Note note) {
        int result = db.update(ArticleNoteContract.TABLE, Note.DATABASE_TABLE.toContentValues(note),
                ArticleNoteContract.Col.ID.getName() + " = ?", new String[]{Long.toString(note.getNoteId())});
        if (result != 1) {
            L.w("Failed to update db entry for note" + note.getNoteId());
        }
    }

    private void deleteNoteFromDb(SQLiteDatabase db, @NonNull Note note) {
        int result = db.delete(ArticleNoteContract.TABLE,
                ArticleNoteContract.Col.ID.getName() + " = ?",
                new String[]{Long.toString(note.getNoteId())});
        if (result != 1) {
            L.w("Failed to update db entry for note" + note.getNoteId());
        }
    }

    //This method checks if article currently exists in the database
    private boolean articleExistsInDb(SQLiteDatabase db, @NonNull String title) {
        try (Cursor cursor = db.query(ArticleContract.TABLE, null,
                ArticleContract.Col.ARTICLE_TITLE.getName() + " = ?",
                new String[]{title},
                null, null, null)) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    private SQLiteDatabase getReadableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getWritableDatabase();
    }
}
