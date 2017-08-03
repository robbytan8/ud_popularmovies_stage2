package com.robby.popular_movies_stage_2a.utilities;

import com.robby.popular_movies_stage_2a.entity.Movie;
import com.robby.popular_movies_stage_2a.entity.MovieReview;
import com.robby.popular_movies_stage_2a.entity.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robby on 7/25/2017.
 *
 * @author Robby Tan
 */

public class JSONConverter {

    public static String parseDataToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder("");
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        inputStream.close();
        return builder.toString();
    }

    public static List<Movie> convertDataToMovieEntity(String data) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(object.getString(Movie.TAG_ID));
                movie.setTitle(object.getString(Movie.TAG_TITLE));
                movie.setPosterPath(object.getString(Movie.TAG_POSTER));
                movie.setOverview(object.getString(Movie.TAG_OVERVIEW));
                movie.setVoteAverage(object.getDouble(Movie.TAG_VOTE_AVERAGE));
                movie.setReleaseDate(object.getString(Movie.TAG_RELEASE_DATE));
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static List<MovieReview> convertDataToMovieReviewEntity(String data) {
        List<MovieReview> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                MovieReview review = new MovieReview();
                review.setId(object.getString(MovieReview.TAG_ID));
                review.setAuthor(object.getString(MovieReview.TAG_AUTHOR));
                review.setContent(object.getString(MovieReview.TAG_CONTENT));
                reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static List<MovieTrailer> convertDataToMovieTrailerEntity(String data) {
        List<MovieTrailer> trailers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                MovieTrailer trailer = new MovieTrailer();
                trailer.setId(object.getString(MovieTrailer.TAG_ID));
                trailer.setKey(object.getString(MovieTrailer.TAG_KEY));
                trailer.setName(object.getString(MovieTrailer.TAG_NAME));
                trailers.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }
}
