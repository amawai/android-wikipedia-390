package org.wikipedia.page.notes.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.wikipedia.database.DatabaseTable;
import org.wikipedia.database.column.Column;
import org.wikipedia.database.contract.ArticleNoteContract;
import org.wikipedia.page.notes.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amawai on 27/03/18.
 */

public class ArticleNoteTable extends DatabaseTable<Note> {
    private static final int DB_VER_INTRODUCED = 20;

    public ArticleNoteTable() {
        super(ArticleNoteContract.TABLE, ArticleNoteContract.URI);
    }

    @Override
    public Note fromCursor(Cursor c) {
        Note note = new Note(
                ArticleNoteContract.Col.ARTICLE_ID.val(c),
                ArticleNoteContract.Col.NOTE_CONTENT.val(c)
        );
        note.setId(ArticleNoteContract.Col.ID.val(c));
        return note;
    }



    @NonNull @Override public Column<?>[] getColumnsAdded(int version) {
        switch (version) {
            case DB_VER_INTRODUCED:
                List<Column<?>> cols = new ArrayList<>();
                cols.add(ArticleNoteContract.Col.ID);
                cols.add(ArticleNoteContract.Col.ARTICLE_ID);
                cols.add(ArticleNoteContract.Col.NOTE_CONTENT);
                return cols.toArray(new Column<?>[cols.size()]);
            default:
                return super.getColumnsAdded(version);
        }
    }

    @Override
    protected ContentValues toContentValues(Note row) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleNoteContract.Col.ARTICLE_ID.getName(), row.getArticleId());
        contentValues.put(ArticleNoteContract.Col.NOTE_CONTENT.getName(), row.getNoteContent());
        return contentValues;
    }


    @Override
    protected String getPrimaryKeySelection(@NonNull Note row,
                                            @NonNull String[] selectionArgs) {
        return super.getPrimaryKeySelection(row, ArticleNoteContract.Col.SELECTION);
    }

    @Override
    protected String[] getUnfilteredPrimaryKeySelectionArgs(@NonNull Note row) {
        return new String[] {String.valueOf(row.getNoteId())};
    }

    @Override
    protected int getDBVersionIntroducedAt() {
        return DB_VER_INTRODUCED;
    }
}
