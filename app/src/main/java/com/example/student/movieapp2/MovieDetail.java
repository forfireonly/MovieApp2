package com.example.student.movieapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.student.movieapp2.MainActivity.reviews;
import static com.example.student.movieapp2.MainActivity.trailers;

public class MovieDetail extends AppCompatActivity {
    ImageButton playTrailerButton;
    ImageButton readReviewButton;
    ImageButton mFavButton;

    Boolean isFav;

    MovieDatabase mDb;

    //this will store JSON response from API
    String resultString = null;
    JSONArray movieDetailsJSON;

    public static int numberOfTrails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        final String poster = intent.getStringExtra("MOVIE_POSTER"); //"http://image.tmdb.org/t/p/w780/" + intent.getStringExtra("MOVIE_POSTER");
        //final String poster_internet = "http://image.tmdb.org/t/p/w780/" + intent.getStringExtra("MOVIE_POSTER");
        ImageView moviePoster = findViewById(R.id.movie_poster);
        //Picasso.get().load(poster_internet).into(moviePoster);
        Picasso.get().load(poster).into(moviePoster);


        final String originalTitleString = intent.getStringExtra("TITLE");
        TextView originalTitle = (TextView) findViewById(R.id.original_title);
        originalTitle.setText(originalTitleString);

        final String releaseDateString = intent.getStringExtra("RELEASE_DATE");
        TextView releaseDate = (TextView) findViewById(R.id.release_date);
        releaseDate.setText(releaseDateString);

        final String synopsisString = intent.getStringExtra("PLOT_SYNOPSIS");
        TextView synopsis = (TextView) findViewById(R.id.synopsis);
        synopsis.setText(synopsisString);

        final String ratingString = intent.getStringExtra("VOTE_AVERAGE");
        final TextView rating = (TextView) findViewById(R.id.user_rating);
        rating.setText(ratingString + "/10");

        final String idMovie = intent.getStringExtra("ID");


        mFavButton = (ImageButton) findViewById(R.id.favorite_button);

        mDb = MovieDatabase.getInstance(getApplicationContext());


        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final FavoriteMovie fmov = mDb.movieDao().loadMovieById(Integer.parseInt(idMovie));
                setFavorite((fmov != null)? true : false);
            }
        });

        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FavoriteMovie mov = new FavoriteMovie(
                        Integer.parseInt(idMovie),
                        originalTitleString,
                        releaseDateString,
                        ratingString,
                        synopsisString,
                        poster

                );
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFav) {
                            // delete item
                            mDb.movieDao().deleteMovie(mov);
                        } else {
                            // insert item
                            mDb.movieDao().insertMovie(mov);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setFavorite(!isFav);
                            }
                        });
                    }

                });

            }
        });



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
                    reviews = getMovieReviews(idMovie);
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
            resultString = downloadMovieTrailers.execute(idMovie+"/videos").get();
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

    public ArrayList<String> getMovieReviews(String idMovie) throws ExecutionException, InterruptedException, JSONException {
        DBMovieTrailerQuery downloadMovieTrailers = new DBMovieTrailerQuery();
        resultString = downloadMovieTrailers.execute(idMovie+"/reviews").get();
        Log.v("ResultString", resultString);
        Log.v("ID", idMovie);
        if (resultString != null) {
            JSONObject movie = new JSONObject(resultString);
            movieDetailsJSON = movie.getJSONArray("results");
            for (int i = 0; i < movieDetailsJSON.length(); i++) {
                JSONObject temp_mov = movieDetailsJSON.getJSONObject(i);
                String review = temp_mov.getString("content");
                Log.v("Review", review);
                reviews.add(review);
                //imgUrl[i+1] = "http://image.tmdb.org/t/p/w500/" + temp_mov.getString("poster_path");
                // i++;
            }
        }return reviews;}

    private void setFavorite(Boolean fav){
        if (fav) {
            isFav = true;
            mFavButton.setImageResource(R.drawable.button_remove_from_favorites);
        } else {
            isFav = false;
            mFavButton.setImageResource(R.drawable.button_favorites);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    }

