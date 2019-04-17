package com.example.student.movieapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        String poster =  "http://image.tmdb.org/t/p/w780/" + intent.getStringExtra("MOVIE_POSTER");

        ImageView moviePoster = findViewById(R.id.movie_poster);
        Picasso.get().load(poster).into(moviePoster);

        String originalTitleString = intent.getStringExtra("TITLE");
        TextView originalTitle = (TextView) findViewById(R.id.original_title);
        originalTitle.setText(originalTitleString);

        String releaseDateString = intent.getStringExtra("RELEASE_DATE");
        TextView releaseDate = (TextView) findViewById(R.id.release_date);
        releaseDate.setText(releaseDateString);

        String synopsisString = intent.getStringExtra("PLOT_SYNOPSIS");
        TextView synopsis = (TextView) findViewById(R.id.synopsis);
        synopsis.setText(synopsisString);

        String ratingString = intent.getStringExtra("VOTE_AVERAGE");
        TextView rating = (TextView) findViewById(R.id.user_rating);
        rating.setText(ratingString);

    }
}
