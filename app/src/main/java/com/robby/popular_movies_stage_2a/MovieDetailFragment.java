package com.robby.popular_movies_stage_2a;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.robby.popular_movies_stage_2a.adapter.MovieReviewAdapter;
import com.robby.popular_movies_stage_2a.adapter.MovieTrailerAdapter;
import com.robby.popular_movies_stage_2a.entity.Movie;
import com.robby.popular_movies_stage_2a.entity.MovieReview;
import com.robby.popular_movies_stage_2a.entity.MovieTrailer;
import com.robby.popular_movies_stage_2a.utilities.DbBitmapUtility;
import com.robby.popular_movies_stage_2a.utilities.JSONConverter;
import com.robby.popular_movies_stage_2a.utilities.MovieOpenHelper;
import com.robby.popular_movies_stage_2a.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.AsyncTaskLoader;
//import android.support.v4.content.Loader;


/**
 * A simple {@link Fragment} subclass.
 *
 * @author Robby Tan
 */

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieReview>>, OpenOtherAppDialogFragment.MyDialogListener {

    @BindView(R.id.tv_movie_overview)
    TextView tvMovieOverview;
    @BindView(R.id.tv_movie_rating_detail)
    TextView tvMovieRating;
    @BindView(R.id.tv_movie_release_date_detail)
    TextView tvMovieReleaseDate;
    @BindView(R.id.tv_movie_title_detail)
    TextView tvMovieTitle;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.tv_less)
    TextView tvLess;
    @BindView(R.id.im_movie_poster_detail)
    ImageView ivMoviePoster;
    @BindView(R.id.rv_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.rv_trailers)
    RecyclerView rvTrailers;

    private MovieReviewAdapter movieReviewAdapter;
    private MovieTrailerAdapter movieTrailerAdapter;
    private MovieOpenHelper movieOpenHelper;
    private Movie movie;
    private static final int MOVIE_REVIEW_ID = 919;
    private static final int MOVIE_TRAILER_ID = 920;
    private final LoaderManager.LoaderCallbacks<ArrayList<MovieTrailer>> trailersCallbacks = new LoaderManager.LoaderCallbacks<ArrayList<MovieTrailer>>() {
        @Override
        public Loader<ArrayList<MovieTrailer>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<ArrayList<MovieTrailer>>(getActivity()) {

                ArrayList<MovieTrailer> trailers;

                @Override
                protected void onStartLoading() {
                    if (trailers != null) {
                        deliverResult(trailers);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public ArrayList<MovieTrailer> loadInBackground() {
                    InputStream inputStream;
                    HttpURLConnection connection = null;
                    try {
                        URL url = NetworkUtils.buildUrlDetail(String.valueOf(movie.getId()), "videos");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(6000);
                        connection.connect();
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            trailers = new ArrayList<>();
                            inputStream = new BufferedInputStream(connection.getInputStream());
                            String data = JSONConverter.parseDataToString(inputStream);
                            trailers.addAll(JSONConverter.convertDataToMovieTrailerEntity(data));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                    return trailers;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<MovieTrailer>> loader, ArrayList<MovieTrailer> data) {
            if (null != data) {
                movieTrailerAdapter.setTrailers(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<MovieTrailer>> loader) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(getResources().getString(R.string.send_parcel_movie))) {
            movie = getArguments().getParcelable(getResources().getString(R.string.send_parcel_movie));
        }
        movieReviewAdapter = new MovieReviewAdapter();
        movieTrailerAdapter = new MovieTrailerAdapter(new MyRecyclerViewListener() {
            @Override
            public void onRecyclerViewItemClicked(View view, String key) {
                OpenOtherAppDialogFragment dialogFragment = OpenOtherAppDialogFragment.newInstance(MovieDetailFragment.this);
                dialogFragment.show(getFragmentManager(), key);
            }
        });
        movieOpenHelper = ((MainActivity) getActivity()).getMovieOpenHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movie_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        tvMovieOverview.setText(movie.getOverview());
        tvMovieRating.setText(String.valueOf(movie.getVoteAverage()) + " / 10");
        tvMovieTitle.setText(movie.getTitle());
        tvMovieReleaseDate.setText(movie.getReleaseDate().substring(0, 4));
        if (NetworkUtils.hasInternetConnection(getActivity())) {
            Picasso.with(getActivity())
                    .load(movie.getPosterPath())
                    .into(ivMoviePoster);
        } else {
            ivMoviePoster.setImageBitmap(movie.getPoster());
        }
        tvMovieOverview.post(new Runnable() {
            @Override
            public void run() {
                if (tvMovieOverview.getLineCount() > 5 && tvMore.getVisibility() == View.INVISIBLE) {
                    tvMore.setVisibility(View.VISIBLE);
                } else if (tvMovieOverview.getLineCount() > 5 && tvMore.getVisibility() == View.VISIBLE) {
//                        showMore();
                } else if (tvMovieOverview.getLineCount() > 5 && tvLess.getVisibility() == View.VISIBLE) {
//                        showLess();
                }
            }
        });
        rvReviews.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvReviews.setHasFixedSize(true);
        rvReviews.setAdapter(movieReviewAdapter);

        rvTrailers.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvTrailers.setHasFixedSize(true);
        rvTrailers.setAdapter(movieTrailerAdapter);
        this.loadMovieReviewsAndTrailers();
    }

    private void loadMovieReviewsAndTrailers() {
        if (NetworkUtils.hasInternetConnection(getActivity())) {
            if (getActivity().getLoaderManager().getLoader(MOVIE_REVIEW_ID) == null) {
                getActivity().getLoaderManager().initLoader(MOVIE_REVIEW_ID, null, this);
            } else {
                getActivity().getLoaderManager().restartLoader(MOVIE_REVIEW_ID, null, this);
            }

            if (getActivity().getLoaderManager().getLoader(MOVIE_TRAILER_ID) == null) {
                getActivity().getLoaderManager().initLoader(MOVIE_TRAILER_ID, null, trailersCallbacks);
            } else {
                getActivity().getLoaderManager().restartLoader(MOVIE_TRAILER_ID, null, trailersCallbacks);
            }
        } else {
            Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<ArrayList<MovieReview>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieReview>>(this.getActivity()) {

            ArrayList<MovieReview> reviews;

            @Override
            protected void onStartLoading() {
                if (reviews != null) {
                    deliverResult(reviews);
                } else {
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieReview> loadInBackground() {
                InputStream inputStream;
                HttpURLConnection connection = null;
                try {
                    URL url = NetworkUtils.buildUrlDetail(String.valueOf(movie.getId()), "reviews");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(6000);
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        reviews = new ArrayList<>();
                        inputStream = new BufferedInputStream(connection.getInputStream());
                        String data = JSONConverter.parseDataToString(inputStream);
                        reviews.addAll(JSONConverter.convertDataToMovieReviewEntity(data));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return reviews;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieReview>> loader, ArrayList<MovieReview> movieReviews) {
        if (null != movieReviews) {
            movieReviewAdapter.setReviews(movieReviews);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieReview>> loader) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialogFragment) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(BuildConfig.MOVIE_VIEW.concat(dialogFragment.getTag())));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.ib_favorite)
    public void markMovieAsFavorite() {
        movieOpenHelper.addFavoriteMovie(movie.getId(), movie.getTitle(), movie.getOverview(),
                DbBitmapUtility.getBytes(movie.getPoster()), movie.getVoteAverage(),
                movie.getReleaseDate());
        Toast.makeText(getActivity(), movie.getReleaseDate(), Toast.LENGTH_SHORT).show();
    }
}
