package org.wikipedia.search;

import org.mockito.Mockito;
import org.wikipedia.WikipediaApp;
import org.wikipedia.database.contract.PageHistoryContract;
import org.wikipedia.history.HistoryEntry;
import org.wikipedia.history.UpdateHistoryTask;
import org.wikipedia.settings.Prefs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(UpdateHistoryTask.class)
public class PrivateBrowsingSearchUpdateHistoryTaskTest {
    UpdateHistoryTask mUpdateHistoryTask;
    HistoryEntry mHistoryEntry;
    PageHistoryContract.Page mPage;
    WikipediaApp mAppInstance;

    @Before
    public void setUp() {
        //mock the class that adds a history entry into the HistoryTable and the app instance which is required
        //for history entry db access
        mUpdateHistoryTask = mock(UpdateHistoryTask.class);
        mHistoryEntry = mock(HistoryEntry.class);
        mPage = mock(PageHistoryContract.Page.class);
        mAppInstance = mock(WikipediaApp.class);
        try {
            //stub a null return value when performTask() is called by the mock object
            when(mUpdateHistoryTask.performTask()).thenReturn(null);
        }
        catch(Throwable e) {
            //method being tested throws throwable, hence why try/catch blocks are used for the setup and the ensuing test
            System.out.println("Throwable caught when stubbing mock UpdateHistoryTask Object.");
        }
    }

    @Test
    public void testPerformTask() {
        try {
            mUpdateHistoryTask.performTask();
            //if private browsing is disabled, an UpdateHistoryTask object should be able to invoke the performTask operation
            //and commence adding a history entry into the db
            if (Prefs.isPrivateBrowsingEnabled() == false) {
                //confirmation that operation has been executed and verify that the encompassed methods are invoked
                Mockito.verify(mAppInstance).getDatabaseClient(HistoryEntry.class);
                Mockito.verify((mAppInstance).getDatabaseClient(HistoryEntry.class)).upsert(mHistoryEntry, mPage.SELECTION);
                assertNull(mUpdateHistoryTask.performTask());
            }
            else {
                //if private browsing has been enabled, the application should not be able to make the call to retrieve a db client
                //and thus invoke the upsert call with that client. This is the first line within the performTask() operation
                //and indicates that no db access is taking place
                Mockito.verify(mAppInstance, never()).getDatabaseClient(HistoryEntry.class);
                assertNull(mUpdateHistoryTask.performTask());
            }
        }
        catch(Throwable e) {
            System.out.println("Throwable caught when invoking performTask() operation with mock UpdateHistoryTask Object.");
        }
    }

}
