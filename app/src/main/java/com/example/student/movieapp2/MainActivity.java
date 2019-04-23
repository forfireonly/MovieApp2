package com.example.student.movieapp2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final String TAG = MainActivity.class.getSimpleName();

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

    FavoritesAdapter.RecyclerViewClickListener listener_2;

    RelativeLayout noInternetConnection;

    public static String movieID;

    private List<FavoriteMovie> favMovs;
    private ArrayList<MovieClass> movieList;

    public static ArrayList<String> favoriteMoviePosters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        spinner =(View) findViewById(R.id.loading_spinner);

        noInternetConnection = (RelativeLayout) findViewById(R.id.no_internet);

        trailers = new ArrayList<>();
        reviews = new ArrayList<>();
        favoriteMoviePosters = new ArrayList<>();
        movieList = new ArrayList<>();


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
        listener_2 = new FavoritesAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int i) throws JSONException, ExecutionException, InterruptedException {
                Toast.makeText(MainActivity.this, "Position " + i, Toast.LENGTH_SHORT).show();
                    Log.v("favorites position", String.valueOf(i) );
                    String title = favMovs.get(i).getTitle();
                    String release_date = favMovs.get(i).getReleaseDate();
                    String vote = favMovs.get(i).getVote();
                    String synopsis = favMovs.get(i).getSynopsis();
                    String image = favMovs.get(i).getImage();

                    //invoke new activity with Intent
                    Intent intent = new Intent(getApplicationContext(), MovieDetail.class);
                    intent.putExtra("TITLE",title);
                    intent.putExtra("MOVIE_POSTER", image);
                    intent.putExtra("RELEASE_DATE",release_date);
                    intent.putExtra("VOTE_AVERAGE",vote);
                    intent.putExtra("PLOT_SYNOPSIS", synopsis);

                    startActivity(intent);
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

        setupViewModel();
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
                //Log.v("Favorite movies number", String.valueOf(favMovs.size()));
                for (int i = 0; i< favMovs.size(); i++) {

                   /* MovieClass mov = new MovieClass(
                            String.valueOf(favMovs.get(i).getId()),
                            favMovs.get(i).getTitle(),
                            favMovs.get(i).getReleaseDate(),
                            favMovs.get(i).getVote(),
                            favMovs.get(i).getSynopsis(),
                            favMovs.get(i).getImage()
                    );
                    movieList.add( mov );*/
                    favoriteMoviePosters.add(favMovs.get(i).getImage());
                }

                mAdapter = new FavoritesAdapter(this, favoriteMoviePosters, listener);
                Log.v("Size of Favorite movies", String.valueOf(favoriteMoviePosters.size()) );
                Log.v("Movie link", favoriteMoviePosters.get(0));
                recyclerView.setAdapter(mAdapter);
                return true;
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

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favs) {
                if(favs.size()>0) {
                    //favMovs.clear();
                    favMovs = favs;
                }
               // for (int i=0; i<favMovs.size(); i++) {
                 //   Log.d(TAG,favMovs.get(i).getTitle());
               // }
             //   loadMovies();
            }
        });
    }


    // check if we are connected to a network
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
