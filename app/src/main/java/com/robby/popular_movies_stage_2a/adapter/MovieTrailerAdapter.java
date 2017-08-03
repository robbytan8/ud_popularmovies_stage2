package com.robby.popular_movies_stage_2a.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.robby.popular_movies_stage_2a.MyRecyclerViewListener;
import com.robby.popular_movies_stage_2a.R;
import com.robby.popular_movies_stage_2a.entity.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Robby on 7/29/2017.
 *
 * @author Robby Tan
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerHolder>{

    private ArrayList<MovieTrailer> trailers = new ArrayList<>();
    private Context context;
    private MyRecyclerViewListener recyclerViewListener;

    public MovieTrailerAdapter(MyRecyclerViewListener recyclerViewListener) {
        this.recyclerViewListener = recyclerViewListener;
    }

    @Override
    public MovieTrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.movie_trailer_row, parent, false);
        return new MovieTrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerHolder holder, final int position) {
        Picasso.with(context)
                .load("http://img.youtube.com/vi/".concat(trailers.get(position).getKey()).concat("/hqdefault.jpg"))
                .into(holder.imMovieTrailerThumbnail);
        holder.ibPlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewListener.onRecyclerViewItemClicked(view, trailers.get(position).getKey());
            }
        });
        holder.tvMovieTrailerName.setText(trailers.get(position).getName());
    }

    public ArrayList<MovieTrailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<MovieTrailer> trailers) {
        this.trailers.clear();
        this.trailers.addAll(trailers);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class MovieTrailerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.im_movie_trailer_thumbnail)
        ImageView imMovieTrailerThumbnail;
        @BindView(R.id.tv_movie_trailer_name)
        TextView tvMovieTrailerName;
        @BindView(R.id.ib_play_trailer)
        ImageButton ibPlayTrailer;

        public MovieTrailerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
