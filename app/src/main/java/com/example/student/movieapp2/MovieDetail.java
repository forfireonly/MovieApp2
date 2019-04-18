package com.example.student.movieapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {
    ImageButton playTrailerButton;
    ImageButton readReviewButton;

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
        rating.setText(ratingString + "/10");

        playTrailerButton = (ImageButton) findViewById(R.id.play_trailer_button);
        playTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startTrailersActivity = new Intent(getBaseContext(), TrailersActivity.class);
                startActivity(startTrailersActivity);
            }
        });

        readReviewButton = (ImageButton) findViewById(R.id.read_review_button);
        readReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startReviewsActivity = new Intent(getBaseContext(), ReviewsActivity.class);
                startActivity(startReviewsActivity);
            }
        });

    }
}
