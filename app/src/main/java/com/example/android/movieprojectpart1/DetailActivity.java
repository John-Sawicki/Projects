package com.example.android.movieprojectpart1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieprojectpart1.sqliteData.FavoriteCheck;
import com.example.android.movieprojectpart1.sqliteData.FavoriteDbHelper;
import com.example.android.movieprojectpart1.sqliteData.FavoriteMovieContract;
import com.example.android.movieprojectpart1.sqliteData.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.android.movieprojectpart1.utilities.API;
import com.example.android.movieprojectpart1.utilities.JsonUtility;
import com.example.android.movieprojectpart1.utilities.ReviewActivity;
import com.example.android.movieprojectpart1.utilities.ReviewAdapter;
import com.example.android.movieprojectpart1.utilities.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TAOnClickHandler{
    private TextView titleText, releaseDateText, userRatingText, overviewText;
    private ImageView moviePosterImage;
     String[] movieData, posterKeyArray, reviewerName = new String[10], reviewerUrl= new String[10];
     String[] validAuthorArray;
    private String [][] movieReviewArray = new String[10][2];
    private String movieId = ""+157336;
    private String movieDetailUrl, youTubeUrl="https://www.youtube.com/watch?v=";   //base url with no key
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerRV;
    private Spinner mSpinner;
    private Button reviewerButton;
    private String reviewerNameSpinner;
    private boolean spinnerUpdated = false;
    ArrayAdapter aa;
    public SQLiteDatabase mDb;
    private ImageButton favButton;
    private boolean favoriteEnabled =false; private boolean cursorFavorite = false;
    FavoriteCheck mFavoriteCheck = new FavoriteCheck(this);
    private boolean spinnerAtStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        moviePosterImage = findViewById(R.id.moviePoster);
        titleText = findViewById(R.id.movieTitle);
        releaseDateText = findViewById(R.id.movieReleased);
        userRatingText = findViewById(R.id.movieVote);
        overviewText = findViewById(R.id.movieOverview);
        mTrailerRV = findViewById(R.id.trailer_recycler_view);
        favButton = findViewById(R.id.fav_button);
        reviewerButton = findViewById(R.id.reviewButton);
        LinearLayoutManager layoutManager = new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL, false );
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRV.setLayoutManager(layoutManager);
        mTrailerRV.setAdapter(mTrailerAdapter);
        for(int i = 0; i<10; i++){
            movieReviewArray[i][0]= "0"; movieReviewArray[i][1]= "0";
            reviewerName[i]="0"; reviewerUrl[i]="0";
        }
        Intent intent = getIntent();
        if(intent.hasExtra("detail data")){   //if(intent.hasExtra("detail data")==true     before lint
            movieData= intent.getStringArrayExtra("detail data");
            Picasso.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w185"+movieData[0])
                    .into(moviePosterImage);
            titleText.setText(movieData[1]);
            releaseDateText.setText(movieData[2]);
            userRatingText.setText(movieData[3]);
            overviewText.setText(movieData[4]);
            movieId = movieData[5]; //use the movie id to create a new url to get trailers
            Log.d("movieId",movieId );
            movieDetailUrl = "https://api.themoviedb.org/3/movie/"+movieId+"?api_key="
                    +API.key +"&append_to_response=videos,reviews";
            Log.d("detail movie", movieDetailUrl);
            new GetDetailTrailerInfoTask().execute(movieDetailUrl);
            Log.d("review author", movieReviewArray[1][0]);
            Log.d("review", movieReviewArray[9][1]);
        }
        cursorFavorite =mFavoriteCheck.CheckForFavorite(movieData[1]);
        Log.d("fav cursor boolean", cursorFavorite+"");
        if (cursorFavorite) {
            favButton.setColorFilter(getResources().getColor(R.color.yellow));
        }else {
            favButton.setColorFilter(getResources().getColor(R.color.gray));
        }
        favButton.setBackgroundColor(getResources().getColor(R.color.white));
        mSpinner= findViewById(R.id.reviewer_spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(reviewerUrl[i]!="0"){
                    reviewerNameSpinner=reviewerUrl[i];
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        reviewerButton.setOnClickListener(new View.OnClickListener() {  //get the reviewer name from the spinner and go the the reviewer url

            @Override
            public void onClick(View view) {
                Log.d("reviewer", reviewerNameSpinner);
                Intent reviewerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewerNameSpinner));
                startActivity(reviewerIntent);
            }
        });
         aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(aa);   //Setting the ArrayAdapter data on the Spinner
        FavoriteDbHelper dbHelper = new FavoriteDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteEnabled = !favoriteEnabled; //when the star is pressed, favorite is enable to enter the db

                if(favoriteEnabled&& !cursorFavorite){//add to db when star is pressed and the movie isn't already in the db
                    Log.d("stat", "favorited");
                    favButton.setColorFilter(getResources().getColor(R.color.yellow));
                    ContentValues cv = new ContentValues();
                    cv.put(FavoriteMovieEntry.POSTER_PATH, movieData[0]);
                    cv.put(FavoriteMovieEntry.ORIGINAL_TITLE, movieData[1]);
                    cv.put(FavoriteMovieEntry.RELEASE_DATE, movieData[2]);
                    cv.put(FavoriteMovieEntry.VOTE_AVERAGE, movieData[3]);
                    cv.put(FavoriteMovieEntry.OVERVIEW, movieData[4]);
                    cv.put(FavoriteMovieEntry.ID, movieData[5]);
                    mDb.insert(FavoriteMovieEntry.TABLE_NAME,null,cv);
                }else {//infavorite to remove from db
                    Log.d("stat", "unfavorited");
                    favButton.setColorFilter(getResources().getColor(R.color.gray));
                    String[] whereArgs = {movieData[1]};    //delete the movie in the detail screen
                    int rowDeleted =mDb.delete(FavoriteMovieEntry.TABLE_NAME,
                            FavoriteMovieEntry.ORIGINAL_TITLE+" = ?",whereArgs);

                }
            }
        });
    }
    @Override
    public void onClick(String trailerName) {
        Log.d("trailer url clicked",movieDetailUrl+trailerName);
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youTubeUrl+trailerName));
        startActivity(youTubeIntent);
    }
    public class GetDetailTrailerInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {    //pass in url   return 2darray of parsed json
            try{
                String jsonStringFromWeb = JsonUtility.getResponseFromSite(strings[0]);

                return jsonStringFromWeb;
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String jsonRawString) {//json string sent to onPost so 2 seperate arrays are created, one for reviews and one for trailers
            String[] youtubeKeyArray;
            String[][] reviewArray;
            try{
                youtubeKeyArray = JsonUtility.formatDetailTrailerJson(jsonRawString);
                posterKeyArray = youtubeKeyArray;
                mTrailerAdapter.setTrailerUrlKeys(posterKeyArray);
                reviewArray = JsonUtility.formatDetailReviewJson(jsonRawString);
                Log.d("reviewArray",reviewArray[1][0] );
                movieReviewArray = reviewArray;
                Log.d("onPost author 2d",movieReviewArray[1][0] );
                Log.d("onPost url 2d",movieReviewArray[1][1] );
                for(int i=0; i<10; i++){//intent only works with 2d arrays
                reviewerName[i]=movieReviewArray[i][0]; //values shown in the spinner
                reviewerUrl[i]=movieReviewArray[i][1];  //values for the url
                    if(reviewerName[i]!="0"){
                        validAuthorArray =reviewerName;  Log.d("validAuthorArray", validAuthorArray[i]);
                    }
                }
                Log.d("onPost author 1d",reviewerName[1]);
                Log.d("onPost url 1d",reviewerUrl[1] );
                /*
                aa.add("");//add nothing for the first reviewer to prevent screen from going to the review automatically
                for(int i =1; i<reviewerName.length+1;i++){
                    Log.d("spinner adapter", reviewerName[i]);
                    if(reviewerName[i]!="0"){
                        aa.add(reviewerName[i]);
                    }
                }
                */

                for(int i =0; i<reviewerName.length;i++){
                    Log.d("spinner adapter", reviewerName[i]);
                    if(reviewerName[i]!="0")  aa.add(reviewerName[i]);
                }

                spinnerUpdated=true;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("favorite", favoriteEnabled);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        favoriteEnabled= savedInstanceState.getBoolean("favorite");
        if(favoriteEnabled){
            favButton.setColorFilter(getResources().getColor(R.color.yellow));
        }
    }
}
