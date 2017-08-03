package com.robby.popular_movies_stage_2a.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.robby.popular_movies_stage_2a.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Robby on 7/25/2017.
 * @author Robby Tan
 */

public class NetworkUtils {

    private static final String PARAM_API = "api_key";

    public static URL buildUrl(String typeRequest) {
        Uri uri = Uri.parse(typeRequest).buildUpon()
                .appendQueryParameter(PARAM_API, BuildConfig.TMDB_KEY)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildUrlDetail(String movieId, String type) {
        Uri uri = Uri.parse(BuildConfig.MOVIE_DETAIL).buildUpon()
                .appendPath(movieId)
                .appendPath(type)
                .appendQueryParameter(PARAM_API, BuildConfig.TMDB_KEY)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasInternetConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
