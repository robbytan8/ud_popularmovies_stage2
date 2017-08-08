package com.robby.popular_movies_stage_2a.utilities;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Robby on 8/6/2017.
 *
 * @author Robby Tan
 */

public class Contract {

    public static final String DATABASE_NAME = "fav_movies";

    public static final String AUTHORITY = "com.robby.popular_movies_stage_2a.utilities.MovieContentProvider";

    public static final String CONTENT_PATH = "movies";
    public static final String COUNT = "count";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_PATH);
    public static final Uri ROW_COUNT_URI = Uri.parse("content://" + AUTHORITY + "/" + COUNT);

    static final String SINGLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.item/vnd.com.robby.provider.movies";
    static final String MULTIPLE_RECORDS_MIME_TYPE =
            "vnd.android.cursor.item/vnd.com.robby.provider.movies";

    private Contract() {}



    public static abstract class MovieList implements BaseColumns {

        public static final String TABLE_NAME = "table_favorite";
        public static final String COL_ID = "_id";
        public static final String COL_TITLE = "title";
        public static final String COL_RATE = "rate";
        public static final String COL_OVERVIEW = "overview";
        public static final String COL_RELEASE = "release_date";
        public static final String COL_POSTER = "poster";
    }
}
