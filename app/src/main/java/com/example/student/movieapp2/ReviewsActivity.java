package com.example.student.movieapp2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import static com.example.student.movieapp2.MainActivity.reviews;

public class ReviewsActivity extends AppCompatActivity {

    ReviewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        RecyclerView recyclerView = findViewById(R.id.trailers_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewsAdapter(this, reviews);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
