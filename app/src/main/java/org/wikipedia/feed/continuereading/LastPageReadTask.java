package org.wikipedia.feed.continuereading;

import android.support.annotation.Nullable;

import org.wikipedia.concurrency.SaneAsyncTask;
import org.wikipedia.history.HistoryEntry;
import org.wikipedia.page.PageBackStackItem;
import org.wikipedia.page.tabs.Tab;
import org.wikipedia.settings.Prefs;

import java.util.List;

public class LastPageReadTask extends SaneAsyncTask<HistoryEntry> {
    private final int age;

    public LastPageReadTask(int age) {
        this.age = age;
    }

    @Nullable @Override public HistoryEntry performTask() throws Throwable {
        List<Tab> tabList = Prefs.getTabs();
        // Make sure that the private browsing button isn't enabled for the article to be shown in
        // the continue reading card
        if (age < tabList.size() && Prefs.isPrivateBrowsingEnabled() == false) {
            List<PageBackStackItem> items = tabList.get(tabList.size() - age - 1).getBackStack();
            if (!items.isEmpty()) {
                return items.get(items.size() - 1).getHistoryEntry();
            }
        }
        return null;
    }
}