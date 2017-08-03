package com.robby.popular_movies_stage_2a.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robby.popular_movies_stage_2a.R;
import com.robby.popular_movies_stage_2a.entity.MovieReview;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Robby on 7/27/2017.
 *
 * @author Robby Tan
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewHolder> {

    private ArrayList<MovieReview> reviews = new ArrayList<>();

    @Override
    public MovieReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_review_row, parent, false);
        return new MovieReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewHolder holder, int position) {
        holder.tvMovieReviewAuthor.setText(reviews.get(position).getAuthor());
        holder.tvMovieReviewContent.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public ArrayList<MovieReview> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<MovieReview> reviews) {
        this.reviews.clear();
        this.reviews.addAll(reviews);
        notifyDataSetChanged();
    }

    class MovieReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_movie_review_author)
        TextView tvMovieReviewAuthor;
        @BindView(R.id.tv_movie_review_content)
        TextView tvMovieReviewContent;

        public MovieReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
