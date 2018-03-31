package org.wikipedia.database.contract;

import android.net.Uri;

import org.wikipedia.database.DbUtil;
import org.wikipedia.database.column.IdColumn;
import org.wikipedia.database.column.IntColumn;
import org.wikipedia.database.column.LongColumn;
import org.wikipedia.database.column.StrColumn;

/**
 * Created by amawai on 27/03/18.
 */


//Stores articles with scroll position and note id
public interface ArticleContract {
    String TABLE = "Articles";
    Uri URI = Uri.withAppendedPath(AppContentProviderContract.AUTHORITY_BASE, "/articles");

    interface Col {
        IdColumn ID = new IdColumn(TABLE);
        StrColumn ARTICLE_TITLE = new StrColumn(TABLE, "articleTitle", "text not null");
        IntColumn SCROLL_POSITION = new IntColumn(TABLE, "scrollPosition", "integer");

        String[] SELECTION = DbUtil.qualifiedNames(ARTICLE_TITLE);
        String[] ALL = DbUtil.qualifiedNames(ID, ARTICLE_TITLE, SCROLL_POSITION);
    }
}
