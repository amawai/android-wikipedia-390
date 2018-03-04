package org.wikipedia.readinglist.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.wikipedia.database.contract.ReadingListPageContract;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Created by amawai on 27/02/18.
 */

@RunWith(RobolectricTestRunner.class)
public class ReadingListDbHelperTester {
    SQLiteDatabase mockDb;
    ReadingList mockReadingList;

    ReadingListDbHelper dbHelper;

    @Before
    public void setUp() {
        dbHelper = new ReadingListDbHelper();
        mockDb = mock(SQLiteDatabase.class);
        mockReadingList = mock(ReadingList.class);
        when(mockReadingList.id()).thenReturn((long)1);
        when(mockDb.query(any(),any(),any(),any(),any(),any(),any())).thenReturn(mock(Cursor.class));
    }

    @Test
    public void shouldQueryReadingListTable() {
        dbHelper.getLinks(mockReadingList, mockDb);
        //Ensure that the correct query arguments are made
        verify(mockDb).query(ReadingListPageContract.TABLE, null,
                ReadingListPageContract.Col.LISTID.getName() + " = ? AND "
                        + ReadingListPageContract.Col.STATUS.getName() + " != ?",
                new String[]{Long.toString(mockReadingList.id()), Integer.toString(ReadingListPage.STATUS_QUEUE_FOR_DELETE)},
                null, null, null);
    }

    @Test
    public void shouldReturnHashMap() {
        assertEquals(dbHelper.getLinks(mockReadingList, mockDb).getClass(), new HashMap<String, String>().getClass());
    }
}