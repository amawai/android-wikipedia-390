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
    //TODO: test out everything here + make sure everything gets added correctly...
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

    public void updateScrollState(@NonNull Article article, int scroll) {
        SQLiteDatabase db = getWritableDatabase();
        updateScrollState(db, article, scroll);
    }

    public void updateScrollState(@NonNull SQLiteDatabase db, @NonNull Article article, int scroll) {
        db.beginTransaction();
        try {
            updateScrollInDb(db, article, scroll);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addNote(@NonNull Article article, @NonNull Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            addNote(db, article, note);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        //SavedPageSyncService.enqueue();
    }


    public void addNotes(@NonNull Article article, @NonNull List<Note> notes) {
        SQLiteDatabase db = getWritableDatabase();
        addNotes(db, article, notes);
    }

    public void addNotes(@NonNull SQLiteDatabase db, @NonNull Article article, @NonNull List<Note> notes) {
        db.beginTransaction();
        try {
            for (Note note : notes) {
                insertPageInDb(db, article, note);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        //SavedPageSyncService.enqueue();
    }

    public void updatePage(@NonNull Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            updateNoteInDb(db, note);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void insertPageInDb(SQLiteDatabase db, @NonNull Article article, @NonNull Note note) {
        note.setArticleId(article.getId());
        long id = db.insertOrThrow(ArticleNoteContract.TABLE, null,
                Note.DATABASE_TABLE.toContentValues(note));
        article.setId(id);
    }

    private void updateScrollInDb(SQLiteDatabase db, @NonNull Article article, int scroll) {
        article.setScroll(scroll);
        int result = db.update(ArticleContract.TABLE, article.DATABASE_TABLE.toContentValues(article),
                ArticleContract.Col.ID.getName() + " = ?", new String[]{Long.toString(article.getId())});
        if (result != 1) {
            L.w("Failed to update scroll position for article " + article.getArticleTitle() + " at scroll " + scroll);
        }
    }

    private void updateNoteInDb(SQLiteDatabase db, @NonNull Note note) {
        int result = db.update(ArticleNoteContract.TABLE, Note.DATABASE_TABLE.toContentValues(note),
                ArticleNoteContract.Col.NOTE_ID.getName() + " = ?", new String[]{Long.toString(note.getNoteId())});
        if (result != 1) {
            L.w("Failed to update db entry for page " + note.getNoteTitle());
        }
    }

    private void deleteNoteFromDb(SQLiteDatabase db, @NonNull Note note) {
        int result = db.delete(ArticleNoteContract.TABLE,
                ArticleNoteContract.Col.NOTE_ID.getName() + " = ?", new String[]{Long.toString(note.getNoteId())});
        if (result != 1) {
            L.w("Failed to delete db entry for page " + note.getNoteTitle());
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

    private void addNote(SQLiteDatabase db, @NonNull Article article, @NonNull Note note) {
        insertPageInDb(db, article, note);
    }

    private SQLiteDatabase getReadableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        return WikipediaApp.getInstance().getDatabase().getWritableDatabase();
    }
}
