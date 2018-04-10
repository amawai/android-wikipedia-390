package org.wikipedia.translation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.wikipedia.R;
import org.wikipedia.page.ExtendedBottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SunXP on 2018-03-29.
 */

public class TranslationDialog extends ExtendedBottomSheetDialogFragment{


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

    private String textTranslated;
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

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_translation, container);
        progressbar = rootView.findViewById(R.id.translation_progress_bar);
        unbinder = ButterKnife.bind(this, rootView);
        TRANSLATION.setText("Translation");
        try{
            loadTranslation();
        }catch(Exception e) {

        }

        return rootView;
    }

    private void loadTranslation() throws Exception{

        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout translationText = rootView.findViewById(R.id.translation_selected_translated);

        View translation = inflater.inflate(R.layout.item_translation, (ViewGroup) rootView, false);
        progressbar.setVisibility(View.GONE);
        loadLanguageOptions(translation);
        loadSelectedText(translation);
        translationText.addView(translation);
    }

    public void loadLanguageOptions(View translation) {

        //PLACEHOLDER FOR CHOOSING LANGUAGE OPTIONS
        Spinner languageOptions = translation.findViewById(R.id.translation_lang_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.language_options,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageOptions.setAdapter(adapter);

        languageOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1:
                        try {
                            loadTranslatedText(translation,"fr");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            loadTranslatedText(translation,"es");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try {
                            loadTranslatedText(translation,"ru");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        try {
                            loadTranslatedText(translation,"hi");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try {
                            loadTranslatedText(translation,"ja");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadSelectedText(View translation) {
        TextView selectedText = translation.findViewById(R.id.translation_selected);
        selectedText.setText("To translate: \n" + textToTranslate);
    }

    public void loadTranslatedText(View translation) throws Exception{

        TextView translatedText = translation.findViewById(R.id.translation_translated);

        //CALL GOOGLE API
        Translator translator = new Translator();
        textTranslated = translator.execute(textToTranslate,"en","fr").get();

        if(textTranslated.equals("-1")) {
            noTranslationFound();
        }else {
            translatedText.setText("Translated: \n" + textTranslated);
        }
    }

    public void loadTranslatedText(View translation, String newTargetLang) throws Exception{
        TextView translatedText = translation.findViewById(R.id.translation_translated);

        //CALL GOOGLE API
        Translator translator = new Translator();
        textTranslated = translator.execute(textToTranslate,"en",newTargetLang).get();

        if(textTranslated.equals("-1")) {
            noTranslationFound();
        }else {
            translatedText.setText("Translated: \n" + textTranslated);
        }
    }

    public void noTranslationFound() {
        noTranslationFound = rootView.findViewById(R.id.translation_not_found);
        noTranslationFound.setText("No translation was found.");
        noTranslationFound.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
    }
}
