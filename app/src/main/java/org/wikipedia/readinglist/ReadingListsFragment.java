package org.wikipedia.readinglist;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.wikipedia.Constants;
import org.wikipedia.R;
import org.wikipedia.WikipediaApp;
import org.wikipedia.analytics.ReadingListsFunnel;
import org.wikipedia.auth.AccountUtil;
import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.feed.FeedFragment;
import org.wikipedia.history.SearchActionModeCallback;
import org.wikipedia.onboarding.OnboardingView;
import org.wikipedia.readinglist.database.ReadingList;
import org.wikipedia.readinglist.database.ReadingListDbHelper;
import org.wikipedia.readinglist.database.ReadingListPage;
import org.wikipedia.readinglist.sync.ReadingListSyncEvent;
import org.wikipedia.settings.Prefs;
import org.wikipedia.settings.SettingsActivity;
import org.wikipedia.util.FeedbackUtil;
import org.wikipedia.util.ReleaseUtil;
import org.wikipedia.views.DrawableItemDecoration;
import org.wikipedia.views.SearchEmptyView;
import org.wikipedia.views.TextInputDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v4.content.ContextCompat.startActivity;

public class ReadingListsFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.reading_list_content_container) ViewGroup contentContainer;
    @BindView(R.id.reading_list_list) RecyclerView readingListView;
    @BindView(R.id.empty_container) View emptyContainer;
    @BindView(R.id.empty_title) TextView emptyTitle;
    @BindView(R.id.empty_image) ImageView emptyImage;
    @BindView(R.id.empty_message) TextView emptyMessage;
    @BindView(R.id.search_empty_view) SearchEmptyView searchEmptyView;
    @BindView(R.id.reading_list_onboarding_container) ViewGroup onboardingContainer;

    private List<ReadingList> readingLists = new ArrayList<>();

    private ReadingListsFunnel funnel = new ReadingListsFunnel();
    private EventBusMethods eventBusMethods = new EventBusMethods();

    private ReadingListAdapter adapter = new ReadingListAdapter();
    private ReadingListItemCallback listItemCallback = new ReadingListItemCallback();
    private ReadingListsSearchCallback searchActionModeCallback = new ReadingListsSearchCallback();
    @Nullable private ActionMode actionMode;

    @NonNull public static ReadingListsFragment newInstance() {
        return new ReadingListsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WikipediaApp.getInstance().getRefWatcher().watch(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading_lists, container, false);
        unbinder = ButterKnife.bind(this, view);

        searchEmptyView.setEmptyText(R.string.search_reading_lists_no_results);
        readingListView.setLayoutManager(new LinearLayoutManager(getContext()));
        readingListView.setAdapter(adapter);
        readingListView.addItemDecoration(new DrawableItemDecoration(getContext(), R.attr.list_separator_drawable));

        WikipediaApp.getInstance().getBus().register(eventBusMethods);

        contentContainer.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        WikipediaApp.getInstance().getBus().unregister(eventBusMethods);
        readingListView.setAdapter(null);
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        maybeShowOnboarding();
        updateLists();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_reading_lists, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem sortByNameItem = menu.findItem(R.id.menu_sort_by_name);
        MenuItem sortByRecentItem = menu.findItem(R.id.menu_sort_by_recent);
        int sortMode = Prefs.getReadingListSortMode(ReadingList.SORT_BY_NAME_ASC);
        sortByNameItem.setTitle(sortMode == ReadingList.SORT_BY_NAME_ASC ? R.string.reading_list_sort_by_name_desc : R.string.reading_list_sort_by_name);
        sortByRecentItem.setTitle(sortMode == ReadingList.SORT_BY_RECENT_DESC ? R.string.reading_list_sort_by_recent_desc : R.string.reading_list_sort_by_recent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_name:
                setSortMode(ReadingList.SORT_BY_NAME_ASC, ReadingList.SORT_BY_NAME_DESC);
                return true;
            case R.id.menu_sort_by_recent:
                setSortMode(ReadingList.SORT_BY_RECENT_DESC, ReadingList.SORT_BY_RECENT_ASC);
                return true;
            case R.id.menu_search_lists:
                ((AppCompatActivity) getActivity())
                        .startSupportActionMode(searchActionModeCallback);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (!isAdded()) {
            return;
        }
        if (visible) {
            updateLists();
            maybeShowOnboarding();
        } else if (actionMode != null) {
            actionMode.finish();
        }
    }

    private void updateLists() {
        updateLists(null);
    }

    private void updateLists(@Nullable final String searchQuery) {
        CallbackTask.execute(() -> ReadingListDbHelper.instance().getAllLists(), new CallbackTask.DefaultCallback<List<ReadingList>>() {
            @Override
            public void success(List<ReadingList> lists) {
                if (getActivity() == null) {
                    return;
                }
                readingLists = lists;
                sortLists();
                updateEmptyState(searchQuery);
                maybeDeleteListFromIntent();
            }
        });
    }

    private void updateEmptyState(@Nullable String searchQuery) {
        if (TextUtils.isEmpty(searchQuery)) {
            searchEmptyView.setVisibility(View.GONE);
            if (readingLists.size() == 1) {
                emptyContainer.setVisibility(View.VISIBLE);
                setUpEmptyContainer();
            }
            emptyContainer.setVisibility(readingLists.size() == 1 ? View.VISIBLE : View.GONE);
        } else {
            searchEmptyView.setVisibility(readingLists.isEmpty() ? View.VISIBLE : View.GONE);
            emptyContainer.setVisibility(View.GONE);
        }
        contentContainer.setVisibility(readingLists.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void setUpEmptyContainer() {
        if (!readingLists.get(0).pages().isEmpty()) {
            emptyTitle.setText(getString(R.string.no_user_lists_title));
            emptyMessage.setText(getString(R.string.no_user_lists_msg));
            emptyImage.setImageDrawable(ContextCompat.getDrawable(emptyImage.getContext(), R.drawable.ic_no_user_lists));
        } else {
            emptyTitle.setText(getString(R.string.reading_lists_empty));
            emptyMessage.setText(getString(R.string.reading_lists_empty_message));
            emptyImage.setImageDrawable(ContextCompat.getDrawable(emptyImage.getContext(), R.drawable.no_lists));
        }
    }

    private class ReadingListItemHolder extends RecyclerView.ViewHolder {
        private ReadingListItemView itemView;

        ReadingListItemHolder(ReadingListItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        void bindItem(ReadingList readingList) {
            itemView.setReadingList(readingList, ReadingListItemView.Description.SUMMARY);
        }

        public ReadingListItemView getView() {
            return itemView;
        }
    }

    private final class ReadingListAdapter extends RecyclerView.Adapter<ReadingListItemHolder> {
        @Override
        public int getItemCount() {
            return readingLists.size();
        }

        @Override
        public ReadingListItemHolder onCreateViewHolder(ViewGroup parent, int pos) {
            ReadingListItemView view = new ReadingListItemView(getContext());
            return new ReadingListItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ReadingListItemHolder holder, int pos) {
            holder.bindItem(readingLists.get(pos));
        }

        @Override public void onViewAttachedToWindow(ReadingListItemHolder holder) {
            super.onViewAttachedToWindow(holder);
            holder.getView().setCallback(listItemCallback);
        }

        @Override public void onViewDetachedFromWindow(ReadingListItemHolder holder) {
            holder.getView().setCallback(null);
            super.onViewDetachedFromWindow(holder);
        }
    }

    private class ReadingListItemCallback implements ReadingListItemView.Callback {
        @Override
        public void onClick(@NonNull ReadingList readingList) {
            if (actionMode != null) {
                actionMode.finish();
            }
            startActivity(ReadingListActivity.newIntent(getContext(), readingList));
        }

        @Override
        public void onRename(@NonNull ReadingList readingList) {
            List<String> existingTitles = new ArrayList<>();
            for (ReadingList list : readingLists) {
                existingTitles.add(list.title());
            }
            existingTitles.remove(readingList.title());
            ReadingListTitleDialog.readingListTitleDialog(getContext(), readingList.title(),
                    existingTitles, text -> {
                        readingList.title(text.toString());
                        ReadingListDbHelper.instance().updateList(readingList);

                        updateLists();
                        funnel.logModifyList(readingList, readingLists.size());
                    }).show();
        }

        @Override
        public void onEditDescription(@NonNull ReadingList readingList) {
            TextInputDialog.newInstance(getContext(), new TextInputDialog.DefaultCallback() {
                @Override
                public void onShow(@NonNull TextInputDialog dialog) {
                    dialog.setHint(R.string.reading_list_description_hint);
                    dialog.setText(readingList.description());
                }

                @Override
                public void onSuccess(@NonNull CharSequence text) {
                    readingList.description(text.toString());
                    ReadingListDbHelper.instance().updateList(readingList);
                    updateLists();
                    funnel.logModifyList(readingList, readingLists.size());
                }
            }).show();
        }

        @Override
        public void onDelete(@NonNull ReadingList readingList) {
            deleteList(readingList);
        }

        @Override
        public void onSaveAllOffline(@NonNull ReadingList readingList) {
            ReadingListDbHelper.instance().markPagesForOffline(readingList.pages(), true);
            updateLists();
            showMultiSelectOfflineStateChangeSnackbar(readingList.pages(), true);
        }

        @Override
        public void onRemoveAllOffline(@NonNull ReadingList readingList) {
            ReadingListDbHelper.instance().markPagesForOffline(readingList.pages(), false);
            updateLists();
            showMultiSelectOfflineStateChangeSnackbar(readingList.pages(), false);
        }

        @Override
        public void onShare(@NonNull ReadingList readingList) {
            String listOfArticles = shareList(readingList);
            if (listOfArticles != null) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, listOfArticles);
                startActivity(Intent.createChooser(sharingIntent, listOfArticles));
            }
            else {
                String message = "Please add an article to this reading list.";
                FeedbackUtil.showMessage(getActivity(), message);
            }
        }
    }

    private void showMultiSelectOfflineStateChangeSnackbar(List<ReadingListPage> pages, boolean offline) {
        String message = offline
                ? getResources().getQuantityString(R.plurals.reading_list_article_offline_message, pages.size())
                : getResources().getQuantityString(R.plurals.reading_list_article_not_offline_message, pages.size());
        FeedbackUtil.showMessage(getActivity(), message);
    }

    private void maybeDeleteListFromIntent() {
        if (getActivity().getIntent().hasExtra(Constants.INTENT_EXTRA_DELETE_READING_LIST)) {
            String titleToDelete = getActivity().getIntent()
                    .getStringExtra(Constants.INTENT_EXTRA_DELETE_READING_LIST);
            getActivity().getIntent().removeExtra(Constants.INTENT_EXTRA_DELETE_READING_LIST);
            for (ReadingList list : readingLists) {
                if (list.title().equals(titleToDelete)) {
                    deleteList(list);
                }
            }
        }
    }

    private void deleteList(@Nullable ReadingList readingList) {
        if (readingList != null) {
            showDeleteListUndoSnackbar(readingList);

            ReadingListDbHelper.instance().deleteList(readingList);
            ReadingListDbHelper.instance().markPagesForDeletion(readingList.pages());

            funnel.logDeleteList(readingList, readingLists.size());
            updateLists();
        }
    }

    public String shareList(@NonNull ReadingList readingList) {
        updateLists();
        ReadingListDbHelper dbHelper = ReadingListDbHelper.instance();
        HashMap<String, String> linkMap = dbHelper.getLinks(readingList);
        if (linkMap.size() > 0) {
            String output = "Here is my reading list:\n\n";

            for (String key : linkMap.keySet()){
                output += (key + ": " + linkMap.get(key) + "\n");
            }
            return output;
        }
        else {
            return null;
        }

    }

    private void showDeleteListUndoSnackbar(final ReadingList readingList) {
        Snackbar snackbar = FeedbackUtil.makeSnackbar(getActivity(),
                String.format(getString(R.string.reading_list_deleted), readingList.title()),
                FeedbackUtil.LENGTH_DEFAULT);
        snackbar.setAction(R.string.reading_list_item_delete_undo, v -> {

            ReadingList newList = ReadingListDbHelper.instance().createList(readingList.title(), readingList.description());
            ReadingListDbHelper.instance().addPagesToList(newList, readingList.pages());
            updateLists();
        });
        snackbar.show();
    }

    private void setSortMode(int sortModeAsc, int sortModeDesc) {
        int sortMode = Prefs.getReadingListSortMode(ReadingList.SORT_BY_NAME_ASC);
        if (sortMode != sortModeAsc) {
            sortMode = sortModeAsc;
        } else {
            sortMode = sortModeDesc;
        }
        Prefs.setReadingListSortMode(sortMode);
        sortLists();
        getActivity().invalidateOptionsMenu();
    }

    private void sortLists() {
        ReadingList.sort(readingLists, Prefs.getReadingListSortMode(ReadingList.SORT_BY_NAME_ASC));
        adapter.notifyDataSetChanged();
    }

    private class ReadingListsSearchCallback extends SearchActionModeCallback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            return super.onCreateActionMode(mode, menu);
        }

        @Override
        protected void onQueryChange(String s) {
            updateLists(s.trim());
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            super.onDestroyActionMode(mode);
            actionMode = null;
            updateLists();
        }

        @Override
        protected String getSearchHintString() {
            return getContext().getResources().getString(R.string.search_hint_search_my_lists);
        }
    }

    private class EventBusMethods {
        @Subscribe public void on(ReadingListSyncEvent event) {
            readingListView.post(() -> {
                if (isAdded()) {
                    updateLists();
                }
            });
        }
    }

    private void maybeShowOnboarding() {
        onboardingContainer.removeAllViews();

        // TODO: remove pre-beta flag when ready.
        if (AccountUtil.isLoggedIn() && !Prefs.isReadingListSyncEnabled()
                && Prefs.isReadingListSyncReminderEnabled()
                && ReleaseUtil.isPreBetaRelease()) {
            OnboardingView onboardingView = new OnboardingView(getContext());
            onboardingView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.base20));
            onboardingView.setTitle(R.string.reading_list_sync_reminder_title);
            onboardingView.setText(R.string.reading_list_sync_reminder_text);
            onboardingView.setPositiveAction(R.string.reading_list_sync_reminder_action);
            onboardingContainer.addView(onboardingView);
            onboardingView.setCallback(new SyncReminderOnboardingCallback());

        } else if (!AccountUtil.isLoggedIn() && Prefs.isReadingListLoginReminderEnabled()
                && ReleaseUtil.isPreBetaRelease()) {
            OnboardingView onboardingView = new OnboardingView(getContext());
            onboardingView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.base20));
            onboardingView.setTitle(R.string.reading_list_login_reminder_title);
            onboardingView.setText(R.string.reading_list_login_reminder_text);
            onboardingView.setNegativeAction(R.string.reading_lists_onboarding_got_it);
            onboardingView.setPositiveAction(R.string.menu_login);
            onboardingContainer.addView(onboardingView);
            onboardingView.setCallback(new LoginReminderOnboardingCallback());
        }
    }

    private class SyncReminderOnboardingCallback implements OnboardingView.Callback {
        @Override
        public void onPositiveAction() {
            startActivity(SettingsActivity.newIntent(getContext()));
        }

        @Override
        public void onNegativeAction() {
            Prefs.setReadingListSyncReminderEnabled(false);
            maybeShowOnboarding();
        }
    }

    private class LoginReminderOnboardingCallback implements OnboardingView.Callback {
        @Override
        public void onPositiveAction() {
            if (getParentFragment() instanceof FeedFragment.Callback) {
                ((FeedFragment.Callback) getParentFragment()).onLoginRequested();
            }
        }

        @Override
        public void onNegativeAction() {
            Prefs.setReadingListLoginReminderEnabled(false);
            maybeShowOnboarding();
        }
    }
}
