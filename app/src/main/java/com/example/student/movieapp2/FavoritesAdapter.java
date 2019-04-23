package com.example.student.movieapp2;

import android.content.Context;
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

import static com.example.student.movieapp2.MainActivity.favoriteMoviePosters;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesHolder> {

    private RecyclerViewClickListener mListener;

    public FavoritesAdapter(MainActivity mainActivity, ArrayList<String> favoriteMoviePosters, RecyclerViewClickListener listener) {
        mListener = listener;}

    public interface RecyclerViewClickListener {

        void onClick(View view, int position) throws JSONException, ExecutionException, InterruptedException;
    }

    @NonNull
    @Override
    public FavoritesAdapter.FavoritesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_poster,viewGroup, false);
        return new FavoritesHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesHolder favoritesHolder, int i) {

        Picasso.get().load(favoriteMoviePosters.get(i)).placeholder(R.drawable.more_info).into(FavoritesHolder.movieView);
    }

    @Override
    public int getItemCount() {
        return favoriteMoviePosters.size();
    }

    public static class FavoritesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public static ImageView movieView;
        private RecyclerViewClickListener mListener;

        public FavoritesHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
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
