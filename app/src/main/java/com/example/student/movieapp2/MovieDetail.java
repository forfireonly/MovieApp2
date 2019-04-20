package com.example.student.movieapp2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.student.movieapp2.MainActivity.trailers;

public class MovieDetail extends AppCompatActivity {
    ImageButton playTrailerButton;
    ImageButton readReviewButton;

    //this will store JSON response from API
    String resultString = null;
    JSONArray movieDetailsJSON;

    public static int numberOfTrails;

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

        final String idMovie = intent.getStringExtra("ID");



        playTrailerButton = (ImageButton) findViewById(R.id.play_trailer_button);
        playTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    trailers = getMovieTrailer(idMovie);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent startTrailersActivity = new Intent(getBaseContext(), TrailersActivity.class);
                startActivity(startTrailersActivity);
            }
        });

        readReviewButton = (ImageButton) findViewById(R.id.read_review_button);
        readReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    trailers = getMovieTrailer(idMovie);
                    numberOfTrails = trailers.size();
                    Log.v("Number of trails", String.valueOf(numberOfTrails));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent startReviewsActivity = new Intent(getBaseContext(), ReviewsActivity.class);
                startActivity(startReviewsActivity);
            }
        });
    }
    public ArrayList<String> getMovieTrailer(String idMovie) throws ExecutionException, InterruptedException, JSONException {
        DBMovieTrailerQuery downloadMovieTrailers = new DBMovieTrailerQuery();
            resultString = downloadMovieTrailers.execute(idMovie).get();
            Log.v("ResultString", resultString);
            Log.v("ID", idMovie);
            if (resultString != null) {
                JSONObject movie = new JSONObject(resultString);
                movieDetailsJSON = movie.getJSONArray("results");
                for (int i = 0; i < movieDetailsJSON.length(); i++) {
                    JSONObject temp_mov = movieDetailsJSON.getJSONObject(i);
                    String videoKey = temp_mov.getString("key");
                    Log.v("VIDEO Key", videoKey);
                    trailers.add("http://www.youtube.com/watch?v=" + videoKey);
                    //imgUrl[i+1] = "http://image.tmdb.org/t/p/w500/" + temp_mov.getString("poster_path");
                    // i++;
                }
            }return trailers;}
    }

