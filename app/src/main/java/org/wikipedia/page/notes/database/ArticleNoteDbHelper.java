package org.wikipedia.page.notes.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

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

    @NonNull
    public Article createArticle(@NonNull String title, @NonNull int scroll) {
        SQLiteDatabase db = getWritableDatabase();
        return createArticle(db, title, scroll);
    }

    @NonNull
    public Article createArticle(@NonNull SQLiteDatabase db, @NonNull String title, @NonNull int scroll) {
        db.beginTransaction();
        try {
            Article protoList = new Article(title, scroll);
            long id = db.insertOrThrow(ArticleContract.TABLE, null,
                    Article.DATABASE_TABLE.toContentValues(protoList));
            db.setTransactionSuccessful();
            protoList.setId(id);
            return protoList;
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
