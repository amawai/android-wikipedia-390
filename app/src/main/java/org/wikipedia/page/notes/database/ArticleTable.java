package org.wikipedia.page.notes.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.wikipedia.database.DatabaseTable;
import org.wikipedia.database.column.Column;
import org.wikipedia.database.contract.ArticleContract;
import org.wikipedia.page.notes.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amawai on 27/03/18.
 */

public class ArticleTable extends DatabaseTable<Article> {
    private static final int DB_VER_INTRODUCED = 20;

    public ArticleTable() {
        super(ArticleContract.TABLE, ArticleContract.URI);
    }

    @Override
    public Article fromCursor(Cursor c) {
        Article article = new Article(
                ArticleContract.Col.ARTICLE_TITLE.val(c),
                ArticleContract.Col.SCROLL_POSITION.val(c)
        );
        article.setId(ArticleContract.Col.ID.val(c));
        return article;
    }



    @NonNull @Override public Column<?>[] getColumnsAdded(int version) {
        switch (version) {
            case DB_VER_INTRODUCED:
                List<Column<?>> cols = new ArrayList<>();
                cols.add(ArticleContract.Col.ID);
                cols.add(ArticleContract.Col.ARTICLE_TITLE);
                cols.add(ArticleContract.Col.SCROLL_POSITION);
                return cols.toArray(new Column<?>[cols.size()]);
            default:
                return super.getColumnsAdded(version);
        }
    }

    @Override
    protected ContentValues toContentValues(Article row) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleContract.Col.ARTICLE_TITLE.getName(), row.getArticleTitle());
        contentValues.put(ArticleContract.Col.SCROLL_POSITION.getName(), row.getScrollPosition());
        return contentValues;
    }


    @Override
    protected String getPrimaryKeySelection(@NonNull Article row,
                                                      @NonNull String[] selectionArgs) {
        return super.getPrimaryKeySelection(row, ArticleContract.Col.SELECTION);
    }

    @Override
    protected String[] getUnfilteredPrimaryKeySelectionArgs(@NonNull Article row) {
        return new String[] {row.getArticleTitle()};
    }

    @Override
    protected int getDBVersionIntroducedAt() {
        return DB_VER_INTRODUCED;
    }
}
