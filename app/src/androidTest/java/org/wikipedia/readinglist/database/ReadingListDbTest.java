package org.wikipedia.readinglist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.wikipedia.WikipediaApp;
import org.wikipedia.database.Database;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.page.Namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by amawai on 28/02/18.
 */

public class ReadingListDbTest {
    final static String TITLE = "Title of article";
    final static String URL = "https://en.wikipedia.org/wiki/Title_of_article";

    SQLiteDatabase db;
    Context mContext;
    Database database;
    ReadingListDbHelper dbHelper;
    ReadingList newReadingList;

    @Before
    public void setUp(){
        mContext = getInstrumentation().getContext();
        database = WikipediaApp.getInstance().getDatabase();
        dbHelper = ReadingListDbHelper.instance();
        newReadingList = new ReadingList("Reading List Title", "Some description of reading list");
        newReadingList.id(42);

        db = database.getWritableDatabase();

        //Create a ReadingList
        WikiSite wikiSite = new WikiSite("https://en.wikipedia.org");
        List<ReadingListPage> pages = new ArrayList<ReadingListPage>();
        ReadingListPage page = new ReadingListPage(wikiSite, Namespace.MAIN, "Title of article", (long)0);
        pages.add(page);
        //Add this reading list to the test database
        dbHelper.addPagesToList(db, newReadingList, pages);
    }

    @Test
    public void shouldReturnHashMapWithTitleAndUrl() {
        HashMap<String, String> linkMap = dbHelper.getLinks(newReadingList, db);

        assertTrue(linkMap.keySet().contains(TITLE));
        assertEquals(linkMap.get(TITLE), URL);
    }

    @After
    public void closeDb(){
        //Close and delete the database that was used for testing
        database.close();
        WikipediaApp.getInstance().deleteDatabase(database.getDatabaseName());
    }

}
