package com.robby.popular_movies_stage_2a;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.robby.popular_movies_stage_2a.adapter.MovieAdapter;
import com.robby.popular_movies_stage_2a.entity.Movie;
import com.robby.popular_movies_stage_2a.utilities.Contract;
import com.robby.popular_movies_stage_2a.utilities.JSONConverter;
import com.robby.popular_movies_stage_2a.utilities.MovieOpenHelper;
import com.robby.popular_movies_stage_2a.utilities.NetworkUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.robby.popular_movies_stage_2a.utilities.Contract.MovieList.COL_ID;
import static com.robby.popular_movies_stage_2a.utilities.Contract.MovieList.COL_OVERVIEW;
import static com.robby.popular_movies_stage_2a.utilities.Contract.MovieList.COL_POSTER;
import static com.robby.popular_movies_stage_2a.utilities.Contract.MovieList.COL_RATE;
import static com.robby.popular_movies_stage_2a.utilities.Contract.MovieList.COL_RELEASE;
import static com.robby.popular_movies_stage_2a.utilities.Contract.MovieList.COL_TITLE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final int PROJECT_MOVIE_ID = 918;

    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    @BindView(R.id.rv_movies_grid)
    RecyclerView rvMovies;

    private MovieAdapter movieAdapter = new MovieAdapter();
    public static MovieOpenHelper movieOpenHelper;
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        movieOpenHelper = new MovieOpenHelper(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(layoutManager);
        rvMovies.setHasFixedSize(true);
        rvMovies.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        rvMovies.setAdapter(movieAdapter);
        if (savedInstanceState == null) {
            getMoviesFromTmdb(BuildConfig.POP_URL);
        } else if (savedInstanceState != null
                && savedInstanceState.getParcelableArrayList(getResources().getString(R.string.bundle_parcel_movie)).size() == 0) {
            getMoviesFromTmdb(BuildConfig.POP_URL);
        }

        if (findViewById(R.id.container_detail) != null) {
            twoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                setTitle(getResources().getString(R.string.popular_title));
                getMoviesFromTmdb(BuildConfig.POP_URL);
                return true;
            case R.id.action_top:
                setTitle(getResources().getString(R.string.top_rated_title));
                getMoviesFromTmdb(BuildConfig.TOP_URL);
                return true;
            case R.id.action_fav:
                setTitle(getResources().getString(R.string.favorite_title));
//                getMovieFromSQlite();
                getMovieFromContentProvider();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getResources().getString(R.string.bundle_parcel_movie), movieAdapter.getMovies());
        outState.putString(getResources().getString(R.string.bundle_app_title), getTitle().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieAdapter.setMovies(savedInstanceState.<Movie>getParcelableArrayList(getResources().getString(R.string.bundle_parcel_movie)));
        setTitle(savedInstanceState.getString(getResources().getString(R.string.bundle_app_title)));
        showData();
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie> movies;

            @Override
            protected void onStartLoading() {
                if (null != movies) {
                    deliverResult(movies);
                } else {
                    forceLoad();
                    showLoader();
                }
            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                InputStream inputStream;
                HttpURLConnection connection = null;
                if (bundle != null && bundle.containsKey("WEB_DIRECTION")) {
                    try {
                        URL url = NetworkUtils.buildUrl(bundle.getString("WEB_DIRECTION"));
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(6000);
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            movies = new ArrayList<>();
                            inputStream = new BufferedInputStream(connection.getInputStream());
                            String moviesInString = JSONConverter.parseDataToString(inputStream);
                            movies.addAll(JSONConverter.convertDataToMovieEntity(moviesInString));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                }
                return movies;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        if (null != movies) {
            movieAdapter.setMovies(movies);
            showData();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    private void showLoader() {
        pbLoader.setVisibility(View.VISIBLE);
        rvMovies.setVisibility(View.INVISIBLE);
    }

    public void showData() {
        pbLoader.setVisibility(View.INVISIBLE);
        rvMovies.setVisibility(View.VISIBLE);
    }

    private void getMoviesFromTmdb(String url) {
        if (NetworkUtils.hasInternetConnection(this)) {
            Bundle bundle = new Bundle();
            bundle.putString("WEB_DIRECTION", url);
            if (getSupportLoaderManager().getLoader(PROJECT_MOVIE_ID) == null) {
                getSupportLoaderManager().initLoader(PROJECT_MOVIE_ID, bundle, this);
            } else {
                getSupportLoaderManager().restartLoader(PROJECT_MOVIE_ID, bundle, this);
            }
        } else {
            Toast.makeText(this, "Please check your internet connection!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMovieFromSQlite() {
        if (movieOpenHelper.count(null) != 0) {
            movieAdapter.setMovies(movieOpenHelper.query());
        } else {
            Toast.makeText(this, "Favorite list is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMovieFromContentProvider() {
        /*
        Cursor cursor =
                mContext.getContentResolver().query(Uri.parse(
                        queryUri), null, null, null, sortOrder);
        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                int indexWord = cursor.getColumnIndex(Contract.WordList.KEY_WORD);
                word = cursor.getString(indexWord);
                holder.wordItemView.setText(word);
                int indexId = cursor.getColumnIndex(Contract.WordList.KEY_ID);
                id = cursor.getInt(indexId);
            } else {
                holder.wordItemView.setText(R.string.error_no_word);
            }
            cursor.close();
        } else {
            Log.e (TAG, "onBindViewHolder: Cursor is null.");
        }
         */
//        if (movieOpenHelper.count(null) != 0) {
//            movieAdapter.setMovies(movieOpenHelper.query());
//        } else {
//        }
        Cursor cursor = getContentResolver().query(Contract.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ArrayList<Movie> movies = new ArrayList<>();
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
                movieAdapter.setMovies(movies);
            }
        } else {
            Toast.makeText(this, "Favorite list is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isTwoPane() {
        return twoPane;
    }
}
