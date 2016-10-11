package ru.sfedu.calendarsfedu;

import android.content.SearchRecentSuggestionsProvider;


public class SearchableProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = "ru.sfedu.calendarsfedu.SearchableProvider";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchableProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }


}
