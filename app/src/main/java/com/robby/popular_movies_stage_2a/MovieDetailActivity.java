package com.robby.popular_movies_stage_2a;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.robby.popular_movies_stage_2a.entity.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_activity);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        Movie m = getIntent().getParcelableExtra(getResources().getString(R.string.send_parcel_movie));
        Bundle arguments = new Bundle();
        arguments.putParcelable(getResources().getString(R.string.send_parcel_movie), m);
//        arguments.putByteArray(getResources().getString(R.string.bundle_parcel_bitmap),
//                getIntent().getByteArrayExtra(getResources().getString(R.string.bundle_parcel_bitmap)));
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction()
                .add(R.id.container_detail, fragment)
                .commit();
    }
}
