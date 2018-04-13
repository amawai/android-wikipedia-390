package org.wikipedia.main;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.wikipedia.BackPressedHandler;
import org.wikipedia.Constants;
import org.wikipedia.R;
import org.wikipedia.WikipediaApp;
import org.wikipedia.activity.FragmentUtil;
import org.wikipedia.analytics.GalleryFunnel;
import org.wikipedia.analytics.IntentFunnel;
import org.wikipedia.analytics.LoginFunnel;
import org.wikipedia.concurrency.CallbackTask;
import org.wikipedia.feed.FeedFragment;
import org.wikipedia.feed.featured.FeaturedArticleCardView;
import org.wikipedia.feed.image.FeaturedImage;
import org.wikipedia.feed.image.FeaturedImageCard;
import org.wikipedia.feed.news.NewsActivity;
import org.wikipedia.feed.news.NewsItemCard;
import org.wikipedia.feed.view.HorizontalScrollingListCardItemView;
import org.wikipedia.gallery.GalleryActivity;
import org.wikipedia.gallery.ImagePipelineBitmapGetter;
import org.wikipedia.gallery.MediaDownloadReceiver;
import org.wikipedia.history.HistoryEntry;
import org.wikipedia.history.HistoryFragment;
import org.wikipedia.imagesearch.Encoder;
import org.wikipedia.imagesearch.ImageLabeler;
import org.wikipedia.imagesearch.ImageSearchActivity;
import org.wikipedia.imagesearch.ImageSearchFragment;
import org.wikipedia.login.LoginActivity;
import org.wikipedia.navtab.NavTab;
import org.wikipedia.navtab.NavTabFragmentPagerAdapter;
import org.wikipedia.navtab.NavTabLayout;
import org.wikipedia.nearby.NearbyFragment;
import org.wikipedia.page.ExclusiveBottomSheetPresenter;
import org.wikipedia.page.PageActivity;
import org.wikipedia.page.PageTitle;
import org.wikipedia.page.linkpreview.LinkPreviewDialog;
import org.wikipedia.random.RandomActivity;
import org.wikipedia.readinglist.AddToReadingListDialog;
import org.wikipedia.search.SearchFragment;
import org.wikipedia.search.SearchInvokeSource;
import org.wikipedia.settings.Prefs;
import org.wikipedia.util.ClipboardUtil;
import org.wikipedia.util.FeedbackUtil;
import org.wikipedia.util.PermissionUtil;
import org.wikipedia.util.ShareUtil;
import org.wikipedia.util.log.L;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static org.wikipedia.Constants.ACTIVITY_REQUEST_IMAGE_SEARCH;

public class MainFragment extends Fragment implements BackPressedHandler, FeedFragment.Callback,
        NearbyFragment.Callback, HistoryFragment.Callback, SearchFragment.Callback,
        LinkPreviewDialog.Callback {
    @BindView(R.id.fragment_main_view_pager) ViewPager viewPager;
    @BindView(R.id.fragment_main_nav_tab_layout) NavTabLayout tabLayout;
    private Unbinder unbinder;
    private ExclusiveBottomSheetPresenter bottomSheetPresenter = new ExclusiveBottomSheetPresenter();
    private MediaDownloadReceiver downloadReceiver = new MediaDownloadReceiver();
    private MediaDownloadReceiverCallback downloadReceiverCallback = new MediaDownloadReceiverCallback();
    private Uri outputFileUri;
    private List <String> imageLabels;
    private Encoder imageEncoder;
    private ImageLabeler labeler;

    // The permissions request API doesn't take a callback, so in the event we have to
    // ask for permission to download a featured image from the feed, we'll have to hold
    // the image we're waiting for permission to download as a bit of state here. :(
    @Nullable private FeaturedImage pendingDownloadImage;

    public interface Callback {
        void onTabChanged(@NonNull NavTab tab);
        void onSearchOpen();
        void onSearchClose(boolean shouldFinishActivity);
        @Nullable View getOverflowMenuAnchor();
        void updateToolbarElevation(boolean elevate);
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater,
                                                 @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        labeler = new ImageLabeler();

        viewPager.setAdapter(new NavTabFragmentPagerAdapter(getChildFragmentManager()));
        tabLayout.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = ((NavTabFragmentPagerAdapter) viewPager.getAdapter()).getCurrentFragment();
            if (fragment instanceof FeedFragment && item.getOrder() == 0) {
                ((FeedFragment) fragment).scrollToTop();
            }
            viewPager.setCurrentItem(item.getOrder());
            return true;
        });

        if (savedInstanceState == null) {
            handleIntent(getActivity().getIntent());
        }

        imageEncoder = new Encoder();
        labeler = new ImageLabeler();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        downloadReceiver.setCallback(null);
        getContext().unregisterReceiver(downloadReceiver);
    }

    @Override public void onResume() {
        super.onResume();
        getContext().registerReceiver(downloadReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downloadReceiver.setCallback(downloadReceiverCallback);
        // update toolbar, since Tab count might have changed
        getActivity().invalidateOptionsMenu();
        // reset the last-page-viewed timer
        Prefs.pageLastShown(0);
    }

    @Override public void onDestroyView() {
        unbinder.unbind();
        unbinder = null;
        super.onDestroyView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == Constants.ACTIVITY_REQUEST_VOICE_SEARCH
                && resultCode == RESULT_OK && data != null
                && data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) != null) {
            String searchQuery = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            openSearchFragment(SearchInvokeSource.VOICE, searchQuery);
        } else if (requestCode == Constants.ACTIVITY_REQUEST_GALLERY
                && resultCode == GalleryActivity.ACTIVITY_RESULT_PAGE_SELECTED) {
            startActivity(data);
        } else if (requestCode == Constants.ACTIVITY_REQUEST_LOGIN
                && resultCode == LoginActivity.RESULT_LOGIN_SUCCESS) {
            FeedbackUtil.showMessage(this, R.string.login_success_toast);
        } else if (requestCode == ACTIVITY_REQUEST_IMAGE_SEARCH) {
             final boolean isCamera;
                if (data == null || data.getData() == null) {
                    isCamera = true;
                 } else {
                      final String action = data.getAction();
                  if (action == null) {
                      isCamera = false;
                  } else {
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }
            Uri selectedImageUri;
            if (isCamera) {
                selectedImageUri = outputFileUri;
            } else {
                selectedImageUri = data == null ? null : data.getData();
            }
            findImageLabels(selectedImageUri); //After this is complete, imageLabels is populated
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //This asynchronously calls the ImageLabeler to recognize the image and populates imageLabels with relevant labels
    private void findImageLabels(Uri selectedImageUri) {
        String encodedImage = imageEncoder.encodeUriToBase64Binary(getContext(), selectedImageUri);
        updateImageLabels(getContext(), encodedImage);
    }

    private void updateImageLabels(Context context, String encodedImage) {
        CallbackTask.execute(() -> getLabelList(encodedImage), new CallbackTask.DefaultCallback<List <String>>() {
            @Override
            public void success(List <String> list) {
                imageLabels = list;
                openImageSearchFragment(imageLabels);
            }
            @Override
            public void failure(Throwable caught) {
                Toast.makeText(getActivity(), "Failed to find image labels", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayResults() {
        Intent imageSearchDisplay = new Intent(getContext(), ImageSearchActivity.class);
        imageSearchDisplay.putStringArrayListExtra(ImageSearchActivity.IMAGE_LABEL_ARGS,
                                                        (ArrayList <String>) imageLabels);
        startActivity(imageSearchDisplay);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.ACTIVITY_REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (PermissionUtil.isPermitted(grantResults)) {
                    if (pendingDownloadImage != null) {
                        download(pendingDownloadImage);
                    }
                } else {
                    setPendingDownload(null);
                    L.i("Write permission was denied by user");
                    FeedbackUtil.showMessage(this,
                            R.string.gallery_save_image_write_permission_rationale);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void handleIntent(Intent intent) {
        IntentFunnel funnel = new IntentFunnel(WikipediaApp.getInstance());
        if (intent.hasExtra(Constants.INTENT_APP_SHORTCUT_SEARCH)) {
            openSearchFragment(SearchInvokeSource.APP_SHORTCUTS, null);
        } else if (intent.hasExtra(Constants.INTENT_APP_SHORTCUT_RANDOM)) {
            startActivity(RandomActivity.newIntent(getActivity(), RandomActivity.INVOKE_SOURCE_SHORTCUT));
        } else if (Intent.ACTION_SEND.equals(intent.getAction())
                && Constants.PLAIN_TEXT_MIME_TYPE.equals(intent.getType())) {
            funnel.logShareIntent();
            openSearchFragment(SearchInvokeSource.INTENT_SHARE,
                    intent.getStringExtra(Intent.EXTRA_TEXT));
        } else if (Intent.ACTION_PROCESS_TEXT.equals(intent.getAction())
                && Constants.PLAIN_TEXT_MIME_TYPE.equals(intent.getType())
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            funnel.logProcessTextIntent();
            openSearchFragment(SearchInvokeSource.INTENT_PROCESS_TEXT,
                    intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT));
        } else if (intent.hasExtra(Constants.INTENT_SEARCH_FROM_WIDGET)) {
            funnel.logSearchWidgetTap();
            openSearchFragment(SearchInvokeSource.WIDGET, null);
        } else if (intent.hasExtra(Constants.INTENT_EXTRA_DELETE_READING_LIST)) {
            goToTab(NavTab.READING_LISTS);
        } else if (lastPageViewedWithin(1) && !intent.hasExtra(Constants.INTENT_RETURN_TO_MAIN)) {
            startActivity(PageActivity.newIntent(getContext()));
        }
    }

    @Override
    public void onFeedTabListRequested() {
        startActivity(PageActivity.newIntentForTabList(getContext()));
    }

    @Override public void onFeedSearchRequested() {
        openSearchFragment(SearchInvokeSource.FEED_BAR, null);
    }

    @Override public void onFeedVoiceSearchRequested() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        try {
            startActivityForResult(intent, Constants.ACTIVITY_REQUEST_VOICE_SEARCH);
        } catch (ActivityNotFoundException a) {
            FeedbackUtil.showMessage(this, R.string.error_voice_search_not_available);
        }
    }

    @Override
    public void onFeedImageSearchRequested() {
        //called from the onclick of the searchbar
        openImageIntent();
    }

    private void openImageIntent() {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        //avoids media collision
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Manages the camera intent
        final List <Intent> cameraIntents = new ArrayList <Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List <ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Add the gallery option and limit to images
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        // Chooser of filesystem options
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Image Search: Select Source");

        // Add the camera options
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, ACTIVITY_REQUEST_IMAGE_SEARCH);
    }

    @Override public void onFeedSelectPage(HistoryEntry entry) {
        startActivity(PageActivity.newIntentForNewTab(getContext(), entry, entry.getTitle()));
    }

    @Override public void onFeedSelectPageFromExistingTab(HistoryEntry entry) {
        startActivity(PageActivity.newIntentForExistingTab(getContext(), entry, entry.getTitle()));
    }

    @Override public void onFeedAddPageToList(HistoryEntry entry) {
        bottomSheetPresenter.show(getChildFragmentManager(),
                AddToReadingListDialog.newInstance(entry.getTitle(),
                        AddToReadingListDialog.InvokeSource.FEED));
    }

    @Override public void onFeedAddFeaturedPageToList(@NonNull final FeaturedArticleCardView view,
                                                      @NonNull HistoryEntry entry) {
        bottomSheetPresenter.show(getChildFragmentManager(),
                AddToReadingListDialog.newInstance(entry.getTitle(),
                        AddToReadingListDialog.InvokeSource.FEED,
                        new DialogInterface.OnDismissListener() {
                            @Override public void onDismiss(DialogInterface dialogInterface) {
                                view.updateFooter();
                            }
                        }));
    }

    @Override
    public void onFeedRemovePageFromList(@NonNull FeaturedArticleCardView view,
                                         @NonNull HistoryEntry entry) {
        FeedbackUtil.showMessage(getActivity(),
                getString(R.string.reading_list_item_deleted, entry.getTitle().getDisplayText()));
        view.updateFooter();
    }

    @Override public void onFeedSharePage(HistoryEntry entry) {
        ShareUtil.shareText(getContext(), entry.getTitle());
    }

    @Override public void onFeedNewsItemSelected(@NonNull NewsItemCard card, @NonNull HorizontalScrollingListCardItemView view) {
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(), view.getImageView(), getString(R.string.transition_news_item));
        startActivity(NewsActivity.newIntent(getActivity(), card.item(), card.wikiSite()), options.toBundle());
    }

    @Override public void onFeedShareImage(final FeaturedImageCard card) {
        final String thumbUrl = card.baseImage().thumbnail().source().toString();
        final String fullSizeUrl = card.baseImage().image().source().toString();
        new ImagePipelineBitmapGetter(thumbUrl) {
            @Override
            public void onSuccess(@Nullable Bitmap bitmap) {
                if (bitmap != null) {
                    ShareUtil.shareImage(getContext(),
                            bitmap,
                            new File(thumbUrl).getName(),
                            ShareUtil.getFeaturedImageShareSubject(getContext(), card.age()),
                            fullSizeUrl);
                } else {
                    FeedbackUtil.showMessage(MainFragment.this, getString(R.string.gallery_share_error, card.baseImage().title()));
                }
            }
        }.get();
    }

    @Override public void onFeedDownloadImage(FeaturedImage image) {
        if (!(PermissionUtil.hasWriteExternalStoragePermission(getContext()))) {
            setPendingDownload(image);
            requestWriteExternalStoragePermission();
        } else {
            download(image);
        }
    }

    @Override public void onFeaturedImageSelected(FeaturedImageCard card) {
        startActivityForResult(GalleryActivity.newIntent(getActivity(), card.age(),
                card.filename(), card.baseImage(), card.wikiSite(),
                GalleryFunnel.SOURCE_FEED_FEATURED_IMAGE), Constants.ACTIVITY_REQUEST_GALLERY);
    }

    @Override
    public void onLoginRequested() {
        startActivityForResult(LoginActivity.newIntent(getContext(), LoginFunnel.SOURCE_NAV),
                Constants.ACTIVITY_REQUEST_LOGIN);
    }

    @NonNull
    @Override
    public View getOverflowMenuAnchor() {
        Callback callback = callback();
        return callback == null ? viewPager : callback.getOverflowMenuAnchor();
    }

    @Override
    public void updateToolbarElevation(boolean elevate) {
        if (callback() != null) {
            callback().updateToolbarElevation(elevate);
        }
    }

    public void requestUpdateToolbarElevation() {
        Fragment fragment = ((NavTabFragmentPagerAdapter) viewPager.getAdapter()).getCurrentFragment();
        updateToolbarElevation(fragment instanceof FeedFragment
                ? ((FeedFragment) fragment).shouldElevateToolbar() : true);
    }

    @Override public void onLoading() {
        // todo: [overhaul] update loading indicator.
    }

    @Override public void onLoaded() {
        // todo: [overhaul] update loading indicator.
    }

    @Override public void onLoadPage(PageTitle title, int entrySource, @Nullable Location location) {
        showLinkPreview(title, entrySource, location);
    }

    @Override public void onLoadPage(PageTitle title, HistoryEntry entry) {
        startActivity(PageActivity.newIntentForNewTab(getContext(), entry, entry.getTitle()));
    }

    @Override public void onClearHistory() {
        // todo: [overhaul] clear history.
    }

    @Override
    public void onSearchResultCopyLink(@NonNull PageTitle title) {
        copyLink(title.getCanonicalUri());
    }

    @Override
    public void onSearchResultAddToList(@NonNull PageTitle title,
                                        @NonNull AddToReadingListDialog.InvokeSource source) {
        bottomSheetPresenter.show(getChildFragmentManager(),
                AddToReadingListDialog.newInstance(title, source));
    }

    @Override
    public void onSearchResultShareLink(@NonNull PageTitle title) {
        ShareUtil.shareText(getContext(), title);
    }

    @Override
    public void onSearchSelectPage(@NonNull HistoryEntry entry, boolean inNewTab) {
        startActivity(PageActivity.newIntentForNewTab(getContext(), entry, entry.getTitle()));
    }

    @Override
    public void onSearchOpen() {
        Callback callback = callback();
        if (callback != null) {
            callback.onSearchOpen();
        }
    }

    @Override
    public void onSearchClose(boolean launchedFromIntent) {
        SearchFragment fragment = searchFragment();
        if (fragment != null) {
            closeSearchFragment(fragment);
        }

        Callback callback = callback();
        if (callback != null) {
            callback.onSearchClose(launchedFromIntent);
        }
    }

    @Override
    public void onLinkPreviewLoadPage(@NonNull PageTitle title, @NonNull HistoryEntry entry, boolean inNewTab) {
        startActivity(PageActivity.newIntentForNewTab(getContext(), entry, entry.getTitle()));
    }

    @Override
    public void onLinkPreviewCopyLink(@NonNull PageTitle title) {
        copyLink(title.getCanonicalUri());
    }

    @Override
    public void onLinkPreviewAddToList(@NonNull PageTitle title) {
        bottomSheetPresenter.show(getChildFragmentManager(),
                AddToReadingListDialog.newInstance(title,
                        AddToReadingListDialog.InvokeSource.LINK_PREVIEW_MENU));
    }

    @Override
    public void onLinkPreviewShareLink(@NonNull PageTitle title) {
        ShareUtil.shareText(getContext(), title);
    }

    @Override
    public boolean onBackPressed() {
        SearchFragment searchFragment = searchFragment();
        if (searchFragment != null && searchFragment.onBackPressed()) {
            return true;
        }

        Fragment fragment = ((NavTabFragmentPagerAdapter) viewPager.getAdapter()).getCurrentFragment();
        if (fragment instanceof BackPressedHandler && ((BackPressedHandler) fragment).onBackPressed()) {
            return true;
        }

        return false;
    }

    public void setBottomNavVisible(boolean visible) {
        tabLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void onGoOffline() {
        Fragment fragment = ((NavTabFragmentPagerAdapter) viewPager.getAdapter()).getCurrentFragment();
        if (fragment instanceof FeedFragment) {
            ((FeedFragment) fragment).onGoOffline();
        }
    }

    public void onGoOnline() {
        Fragment fragment = ((NavTabFragmentPagerAdapter) viewPager.getAdapter()).getCurrentFragment();
        if (fragment instanceof FeedFragment) {
            ((FeedFragment) fragment).onGoOnline();
        }
    }

    @OnPageChange(R.id.fragment_main_view_pager) void onTabChanged(int position) {
        Callback callback = callback();
        if (callback != null) {
            NavTab tab = NavTab.of(position);
            callback.onTabChanged(tab);
        }
    }

    private void showLinkPreview(PageTitle title, int entrySource, @Nullable Location location) {
        bottomSheetPresenter.show(getChildFragmentManager(), LinkPreviewDialog.newInstance(title, entrySource, location));
    }

    private void copyLink(@NonNull String url) {
        ClipboardUtil.setPlainText(getContext(), null, url);
        FeedbackUtil.showMessage(this, R.string.address_copied);
    }

    private boolean lastPageViewedWithin(int days) {
        return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - Prefs.pageLastShown()) < days;
    }

    private List <String> getLabelList(String imagePath) throws Exception{
        String appendedLabels = labeler.execute(imagePath).get();
        List <String> labelList = new ArrayList <String>(Arrays.asList(appendedLabels.split(",")));
        return labelList;
    }

    private void download(@NonNull FeaturedImage image) {
        setPendingDownload(null);
        downloadReceiver.download(getContext(), image);
        FeedbackUtil.showMessage(this, R.string.gallery_save_progress);
    }

    private void setPendingDownload(@Nullable FeaturedImage image) {
        pendingDownloadImage = image;
    }

    private void requestWriteExternalStoragePermission() {
        PermissionUtil.requestWriteStorageRuntimePermissions(this,
                Constants.ACTIVITY_REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    @SuppressLint("CommitTransaction")
    private void openSearchFragment(@NonNull SearchInvokeSource source, @Nullable String query) {
        Fragment fragment = searchFragment();
        if (fragment == null) {
            fragment = SearchFragment.newInstance(source, StringUtils.trim(query));
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_main_container, fragment)
                    .commitNowAllowingStateLoss();
        }
    }

    @SuppressLint("CommitTransaction")
    private void closeSearchFragment(@NonNull SearchFragment fragment) {
        getChildFragmentManager().beginTransaction().remove(fragment).commitNowAllowingStateLoss();
    }

    public void switchToSearchFragment(ImageSearchFragment fragment,
                                       @NonNull SearchInvokeSource source, @Nullable String query){
        closeImageSearchFragment(fragment);
        openSearchFragment(source, query);
    }

    @SuppressLint("CommitTransaction")
    private void closeImageSearchFragment(@NonNull ImageSearchFragment fragment) {
        getChildFragmentManager().beginTransaction().remove(fragment).commitNowAllowingStateLoss();
    }

    @SuppressLint("CommitTransaction")
    private void openImageSearchFragment(List<String> labels) {
        Fragment fragment = ImageSearchFragment.newInstance(labels);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_main_container, fragment)
                .commitNow();
    }

    @Nullable private SearchFragment searchFragment() {
        return (SearchFragment) getChildFragmentManager().findFragmentById(R.id.fragment_main_container);
    }

    private void cancelSearch() {
        SearchFragment fragment = searchFragment();
        if (fragment != null) {
            fragment.closeSearch();
        }
    }

    private void goToTab(@NonNull NavTab tab) {
        tabLayout.setSelectedItemId(tab.code());
        cancelSearch();
    }

    private class MediaDownloadReceiverCallback implements MediaDownloadReceiver.Callback {
        @Override
        public void onSuccess() {
            FeedbackUtil.showMessage(getActivity(), R.string.gallery_save_success);
        }
    }

    @Nullable private Callback callback() {
        return FragmentUtil.getCallback(this, Callback.class);
    }
}
