package com.robby.popular_movies_stage_2a.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.robby.popular_movies_stage_2a.entity.Movie;

import java.util.ArrayList;

/**
 * Created by Robby on 7/31/2017.
 *
 * @author Robby Tan
 */

public class MovieOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fav_movies";
    private static final String TABLE_NAME = "table_favorite";

    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_RATE = "rate";
    private static final String COL_OVERVIEW = "overview";
    private static final String COL_RELEASE = "release_date";
    private static final String COL_POSTER = "poster";

    private static final String TABLE_CREATION_QUERY = "CREATE TABLE " + TABLE_NAME + " ("
            + COL_ID + " TEXT PRIMARY KEY, "
            + COL_TITLE + " TEXT, "
            + COL_OVERVIEW + " TEXT, "
            + COL_POSTER + " BLOB, "
            + COL_RATE + " REAL, "
            + COL_RELEASE + " TEXT);";

    private SQLiteDatabase mWritableDb;
    private SQLiteDatabase mReadableDb;

    public MovieOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATION_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Movie> query() {
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = null;
        ArrayList<Movie> movies = null;

        try {
            if (mReadableDb == null) {
                mReadableDb = getReadableDatabase();
            }
            cursor = mReadableDb.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                movies = new ArrayList<>();
                do {
                    Movie movie = new Movie();
                    movie.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                    movie.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
                    movie.setOverview(cursor.getString(cursor.getColumnIndex(COL_OVERVIEW)));
                    movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COL_RATE))));
                    movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COL_RELEASE)));
                    movie.setPoster(cursor.getBlob(cursor.getColumnIndex(COL_POSTER)));
                    movies.add(movie);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "QUERY EXCEPTION! " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return movies;
    }

    public long addFavoriteMovie(String id, String title, String overview, byte[] poster, double rate, String release) {
        if (count(id) == 0) {
            long newId = 0;
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_ID, id);
            contentValues.put(COL_TITLE, title);
            contentValues.put(COL_OVERVIEW, overview);
            contentValues.put(COL_POSTER, poster);
            contentValues.put(COL_RATE, rate);
            contentValues.put(COL_RELEASE, release);
            try {
                if (mWritableDb == null) {
                    mWritableDb = getWritableDatabase();
                }
                newId = mWritableDb.insert(TABLE_NAME, null, contentValues);
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "INSERT EXCEPTION : " + e.getMessage());
            }
            return newId;
        } else {
            return -1;
        }
    }

    public int deleteFavoriteMovie(String id) {
        int deleted = 0;
        try {
            if (mWritableDb == null) {
                mWritableDb = getWritableDatabase();
            }
            deleted = mWritableDb.delete(TABLE_NAME,
                    COL_ID + " = ?",
                    new String[]{id});
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "DELETE EXCEPTION : " + e.getMessage());
        }
        return deleted;
    }

    public long count(String id) {
        if (mReadableDb == null) {
            mReadableDb = getReadableDatabase();
        }
        if (id != null) {
            return DatabaseUtils.queryNumEntries(mReadableDb, TABLE_NAME, COL_ID + " = " + id);
        }
        return DatabaseUtils.queryNumEntries(mReadableDb, TABLE_NAME);
    }
}
