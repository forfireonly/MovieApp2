package com.example.student.movieapp2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.student.movieapp2.MainActivity.API_KEY;


public class DBMovieQuery extends AsyncTask<String, Void, String> {


    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String response = null;



    @Override
    protected String doInBackground(String... params) {
        try {
            String choice = params[0];
            URL url = null;

            url = new URL("http://api.themoviedb.org/3/movie/" + choice + "?api_key=" + API_KEY);


            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }

            response = buffer.toString();
            //Log.d("result", response);

        } catch (IOException e) {
            Log.e("DownloadTask", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("DownloadTask", "Error closing stream", e);
                }
            }
        }
        //Log.v("FINAL",response);
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}