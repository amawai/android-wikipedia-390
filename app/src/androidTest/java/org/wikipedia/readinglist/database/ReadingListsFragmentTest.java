package org.wikipedia.readinglist.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.wikipedia.WikipediaApp;
import org.wikipedia.database.Database;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.page.Namespace;
import org.wikipedia.readinglist.ReadingListsFragment;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * Created by SunXP on 2018-03-02.
 */

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(ReadingListTable.class)
public class ReadingListsFragmentTest {
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
    public void shareListTest() {
        ReadingList newReadingList = new ReadingList("Reading List Title", "Some description of reading list");
        ReadingListsFragment readingListsFragment = ReadingListsFragment.newInstance();

        //list is empty, return null
        assertNull((readingListsFragment.shareList(newReadingList)));

        //add article to reading list
        newReadingList.id(42);

        //test
        assertNotNull((readingListsFragment.shareList(newReadingList)));
        assertEquals("Here is my reading list:\n" + "\n"
                + "Title of article: https://en.wikipedia.org/wiki/Title_of_article\n", readingListsFragment.shareList(newReadingList));


    }

}