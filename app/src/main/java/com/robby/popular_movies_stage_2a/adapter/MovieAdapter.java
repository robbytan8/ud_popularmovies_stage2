package com.robby.popular_movies_stage_2a.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.robby.popular_movies_stage_2a.MainActivity;
import com.robby.popular_movies_stage_2a.MovieDetailActivity;
import com.robby.popular_movies_stage_2a.MovieDetailFragment;
import com.robby.popular_movies_stage_2a.R;
import com.robby.popular_movies_stage_2a.entity.Movie;
import com.robby.popular_movies_stage_2a.utilities.DbBitmapUtility;
import com.robby.popular_movies_stage_2a.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Robby on 7/25/2017.
 *
 * @author Robby Tan
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movies = new ArrayList<>();
    private Context context;

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        if (NetworkUtils.hasInternetConnection(context)) {
            Target target = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    movies.get(position).setPoster(DbBitmapUtility.getBytes(bitmap));
                    holder.imMovieGrid.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            holder.imMovieGrid.setTag(target);
            Picasso.with(context)
                    .load(movies.get(position).getPosterPath())
                    .into(target);
        } else {
            holder.imMovieGrid.setImageBitmap(DbBitmapUtility.getImage(movies.get(position).getPoster()));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_movie_grid)
        ImageView imMovieGrid;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imMovieGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((MainActivity) context).isTwoPane()) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(context.getResources().getString(R.string.send_parcel_movie), movies.get(getAdapterPosition()));
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        ((MainActivity) context).getFragmentManager().beginTransaction()
                                .replace(R.id.container_detail, fragment)
                                .commit();
                    } else {
                        Movie movie = movies.get(getAdapterPosition());
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(context.getResources().getString(R.string.send_parcel_movie), movie);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
