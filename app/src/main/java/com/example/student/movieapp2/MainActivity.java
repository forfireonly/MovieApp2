package com.example.student.movieapp2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public static String API_KEY = "YOUR-API-KEY";

    public  String choice = "popular";


    //title, release date, movie poster, vote average, and plot synopsis(overview).
    private final  String TITLE = "title";
    private final  String PLOT_SYNOPSIS = "overview";
    private final  String MOVIE_POSTER = "poster_path";

    private final  String RELEASE_DATE = "release_date";
    private final  String VOTE_AVERAGE = "vote_average";
    private final  String MOVIE_ID = "id";

    //this will store JSON response from API
    String resultString = null;
    JSONArray movieDetailsJSON;

    public static String[] imgUrl = new String[20];

    public static ArrayList<String> trailers;
    public static ArrayList <String> reviews;

    DisplayingMoviesAdapter.RecyclerViewClickListener listener;
    View spinner;

    RelativeLayout noInternetConnection;

    public static String movieID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        spinner =(View) findViewById(R.id.loading_spinner);

        noInternetConnection = (RelativeLayout) findViewById(R.id.no_internet);

        trailers = new ArrayList<>();
        reviews = new ArrayList<>();


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        int spanCount = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;

        // use a grid layout manager
        layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);


        listener = new DisplayingMoviesAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) throws JSONException, ExecutionException, InterruptedException {
                //Toast.makeText(MainActivity.this, "Position " + position, Toast.LENGTH_SHORT).show();
                if (!isOnline()){
                    noInternetConnection.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else
                {JSONObject object = movieDetailsJSON.getJSONObject(position);
                    String title = object.getString(TITLE);
                    String poster = object.getString(MOVIE_POSTER);
                    String release_date = object.getString(RELEASE_DATE);
                    String vote = object.getString(VOTE_AVERAGE);
                    String plot = object.getString(PLOT_SYNOPSIS);

                    movieID =object.getString(MOVIE_ID);
                    Log.v(MOVIE_ID, movieID);

                   // trailers = getTrailers(movieID);
                    //Log.v("Trailers", list.toString());

                    //invoke new activity with Intent
                    Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
                    intent.putExtra("TITLE",title);
                    intent.putExtra("MOVIE_POSTER", poster);
                    intent.putExtra("RELEASE_DATE",release_date);
                    intent.putExtra("VOTE_AVERAGE",vote);
                    intent.putExtra("PLOT_SYNOPSIS",plot);

                    intent.putExtra("ID",movieID);

                    startActivity(intent);}

            }
        };

        // specify an adapter (see also next example)
        try {

            mAdapter = new DisplayingMoviesAdapter(this, getMoviePoster(choice),listener);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(mAdapter);
        // Log.v("choice", choice);
        spinner.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.top_rated:
                choice = "top_rated";
                try {
                    mAdapter = new DisplayingMoviesAdapter(this, getMoviePoster(choice), listener);//"vote_average.desc";
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.setAdapter(mAdapter);
                //Log.v("choice", choice);
                return true;
            case R.id.most_popular:
                choice = "popular";
                // Log.v("choice", choice);
                try {
                    mAdapter = new DisplayingMoviesAdapter(this, getMoviePoster(choice), listener);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.setAdapter(mAdapter);
                return true;
            case R.id.favorites:
        }

        return super.onOptionsItemSelected(item);
    }

    public String[] getMoviePoster(String selection) throws ExecutionException, InterruptedException, JSONException {
        if (!isOnline()){
            noInternetConnection.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else
        {DBMovieQuery downloadMoviePosters = new DBMovieQuery();
            resultString = downloadMoviePosters.execute(selection).get();
            Log.v("choice", choice);
            if (resultString != null) {
                JSONObject movie = new JSONObject(resultString);
                movieDetailsJSON = movie.getJSONArray("results");
                for (int i = 0; i < movieDetailsJSON.length(); i++) {
                    JSONObject temp_mov = movieDetailsJSON.getJSONObject(i);
                    imgUrl[i] = "http://image.tmdb.org/t/p/w500/" + temp_mov.getString("poster_path");
                    //imgUrl[i+1] = "http://image.tmdb.org/t/p/w500/" + temp_mov.getString("poster_path");
                    // i++;
                }
            }}

        return imgUrl;}


    // check if we are connected to a network
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
