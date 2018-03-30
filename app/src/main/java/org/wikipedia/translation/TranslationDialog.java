package org.wikipedia.translation;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.wikipedia.R;
import org.wikipedia.WikipediaApp;
import org.wikipedia.activity.FragmentUtil;
import org.wikipedia.analytics.WiktionaryDialogFunnel;
import org.wikipedia.dataclient.WikiSite;
import org.wikipedia.dataclient.page.PageClient;
import org.wikipedia.dataclient.page.PageClientFactory;
import org.wikipedia.dataclient.restbase.RbDefinition;
import org.wikipedia.dataclient.restbase.page.RbPageClient;
import org.wikipedia.dataclient.restbase.page.RbPageClient.DefinitionCallback;
import org.wikipedia.page.ExtendedBottomSheetDialogFragment;
import org.wikipedia.page.LinkMovementMethodExt;
import org.wikipedia.page.Namespace;
import org.wikipedia.page.PageTitle;
import org.wikipedia.util.StringUtil;
import org.wikipedia.util.log.L;
import org.wikipedia.views.AppTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SunXP on 2018-03-29.
 */

public class TranslationDialog extends ExtendedBottomSheetDialogFragment {
    public interface Callback {
        void translationShowDialog(@NonNull String text);
    }

    private Unbinder unbinder;
    private static final String SELECTED_TEXT = "selected_text";
    @BindView(R.id.translation_dialog_title) TextView TRANSLATION;
    @BindView(R.id.translation_not_found) TextView noTranslationFound;
    //@BindView(R.id.translation_selected_text) TextView selectedText;
    //@BindView(R.id.translation_translated_text) TextView translatedText;

    private static String[] ENABLED_LANGUAGES = {
            "en" // English
    };


    private ProgressBar progressbar;
    private String textToTranslate;
    private View rootView;

    public static TranslationDialog newInstance(@NonNull String selectedText) {
        TranslationDialog dialog = new TranslationDialog();
        Bundle args = new Bundle();
        args.putString(SELECTED_TEXT, selectedText);
        dialog.setArguments(args);
        return dialog;
    }

    public static String[] getEnabledLanguages() {
        return ENABLED_LANGUAGES;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToTranslate = getArguments().getString(SELECTED_TEXT);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_translation, container);
//        progressbar = rootView.findViewById(R.id.translation_dialog_progress);
        unbinder = ButterKnife.bind(this, rootView);
        TRANSLATION.setText("Translation");
        loadTranslation();


        //noTranslationFound();
        //display the selected text
        //call the translation api
        //display the translated text

        return rootView;
    }

    private void loadTranslation() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout translationText = rootView.findViewById(R.id.translation_selected_translated);

        View translation = inflater.inflate(R.layout.item_translation, (ViewGroup) rootView, false);
        loadSelectedText(translation);
        translationText.addView(translation);

    }

    private void loadSelectedText(View translation) {
        TextView selectedText = translation.findViewById(R.id.translation_selected);
        selectedText.setText("To translate: " + textToTranslate);
    }

    private void loadTranslatedText() {

    }

    private void noTranslationFound() {
        noTranslationFound = rootView.findViewById(R.id.translation_not_found);
        noTranslationFound.setText("No translation was found.");
        noTranslationFound.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
    }
}
