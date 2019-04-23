package com.example.student.movieapp2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.student.movieapp2.MainActivity.imgUrl;


public class DisplayingMoviesAdapter extends RecyclerView.Adapter<DisplayingMoviesAdapter.DisplayingMoviesHolder> {

    private RecyclerViewClickListener mListener;

    public interface RecyclerViewClickListener extends FavoritesAdapter.RecyclerViewClickListener {

        void onClick(View view, int position) throws JSONException, ExecutionException, InterruptedException;
    }


    public DisplayingMoviesAdapter(MainActivity mainActivity, String[] moviePoster, RecyclerViewClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public DisplayingMoviesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_poster,viewGroup, false);
        return new DisplayingMoviesHolder(v, mListener);
    }



    @Override
    public void onBindViewHolder(@NonNull DisplayingMoviesHolder displayingMoviesHolder, int i) {

        Picasso.get().load(imgUrl[i]).placeholder(R.drawable.more_info).into(displayingMoviesHolder.movieView);

    }

    @Override
    public int getItemCount() {
        return imgUrl.length;
    }


    public class DisplayingMoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView movieView;

        private RecyclerViewClickListener mListener;

        public DisplayingMoviesHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mListener = listener;
            movieView = (ImageView) itemView.findViewById(R.id.movie_poster_display);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            try {
                mListener.onClick(view, getAdapterPosition());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }
}