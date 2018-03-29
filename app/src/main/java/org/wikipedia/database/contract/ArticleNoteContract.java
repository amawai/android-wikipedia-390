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

//Stores notes associated to an article
public interface ArticleNoteContract {
    String TABLE = "Notes";
    Uri URI = Uri.withAppendedPath(AppContentProviderContract.AUTHORITY_BASE, "/notes");

    interface Col {
        IdColumn NOTE_ID = new IdColumn(TABLE);
        LongColumn ARTICLE_ID = new LongColumn(TABLE, "noteId", "integer");
        StrColumn NOTE_TITLE = new StrColumn(TABLE, "title", "text not null");
        StrColumn NOTE_CONTENT = new StrColumn(TABLE, "noteContent", "text not null");
        IntColumn SCROLL_POSITION = new IntColumn(TABLE, "scrollPosition", "integer");

        String[] SELECTION = DbUtil.qualifiedNames(NOTE_ID);
        String[] ALL = DbUtil.qualifiedNames(NOTE_ID, NOTE_TITLE, NOTE_CONTENT, ARTICLE_ID, SCROLL_POSITION);
    }
}
