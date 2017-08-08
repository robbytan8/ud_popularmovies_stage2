package com.robby.popular_movies_stage_2a.utilities;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Robby on 8/5/2017.
 *
 * @author Robby Tan
 */

public class MovieContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int URI_ALL_ITEM_CODE = 10;
    private static final int URI_COUNT_CODE = 20;

    private MovieOpenHelper movieOpenHelper;

    static {
        uriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH, URI_ALL_ITEM_CODE);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.CONTENT_PATH + "/" + Contract.COUNT, URI_COUNT_CODE);
    }

    @Override
    public boolean onCreate() {
        movieOpenHelper = new MovieOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case URI_ALL_ITEM_CODE:
                cursor = movieOpenHelper.cursorQuery();
                break;
            case URI_COUNT_CODE:
                cursor = movieOpenHelper.countCursor(uri.getEncodedQuery());
                break;
            default:
                Log.e(this.getClass().getName(), "Invalid URI - Not recognized");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_ALL_ITEM_CODE:
                return Contract.MULTIPLE_RECORDS_MIME_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = movieOpenHelper.addFavoriteMovie(contentValues);
        return Uri.parse(Contract.CONTENT_URI + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return movieOpenHelper.deleteFavoriteMovie(strings[0]);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
