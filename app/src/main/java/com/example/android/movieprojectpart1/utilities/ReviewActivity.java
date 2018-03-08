package com.example.android.movieprojectpart1.utilities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.movieprojectpart1.R;

public class ReviewActivity extends AppCompatActivity {
    private String [] reviewerName, reviewerUrl;
    private String mMovieTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent = getIntent();
        if(intent.hasExtra("reviewer info")){
            reviewerName = intent.getStringArrayExtra("reviewer info");//add reviews' name to an array
            reviewerUrl= intent.getStringArrayExtra("review url");//use url to go to tmdb page for the review
            mMovieTitle=intent.getStringExtra("movie title");
        }
    }

}
