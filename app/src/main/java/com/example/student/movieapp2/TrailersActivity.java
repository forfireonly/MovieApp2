package com.example.student.movieapp2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import static com.example.student.movieapp2.MainActivity.trailers;

public class TrailersActivity extends AppCompatActivity implements TrailersAdapter.ItemClickListener  {

    TrailersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        RecyclerView recyclerView = findViewById(R.id.trailers_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrailersAdapter(this, trailers);
        adapter.setClickListener(this);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        String youtube_link = trailers.get(position);

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_link)));
        Log.i("Video", "Video Playing....");

    }

}
