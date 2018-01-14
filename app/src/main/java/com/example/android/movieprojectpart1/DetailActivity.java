package com.example.android.movieprojectpart1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView titleText, releaseDateText, userRatingText, overviewText;
    ImageView moviePoster;
    String movieData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        titleText = findViewById(R.id.movieTitle);
        releaseDateText = findViewById(R.id.releaseTitle);
        Intent intent = getIntent();
        if(intent.hasExtra("detail data")==true){
            movieData= intent.getStringExtra("detail data");
        }
    }
}
