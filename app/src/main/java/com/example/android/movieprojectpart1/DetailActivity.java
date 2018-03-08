package com.example.android.movieprojectpart1;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerRV, mReviewerRV;
    private Spinner mSpinner;
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
        LinearLayoutManager layoutManager = new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL, false );
       //mReviewAdapter = new ReviewAdapter(this);
        //mReviewerRV.setAdapter(mReviewAdapter);
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

            /*
            reviewerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent reviewerIntent =new Intent(DetailActivity.this, ReviewActivity.class);

                    reviewerIntent.putExtra("reviewer info",reviewerName);
                    reviewerIntent.putExtra("review url",reviewerUrl );
                    reviewerIntent.putExtra("movie title",movieData[1] );
                    startActivity(reviewerIntent);
                }
            });
            */
        }

        mSpinner= findViewById(R.id.reviewer_spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("spinner item url",reviewerUrl[i] );
               // Log.d("reviewer url clicked",reviewerUrl[i]);
                if(reviewerUrl[i]!="0"){
                   Intent reviewerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewerUrl[i]));
                    startActivity(reviewerIntent);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,reviewerName);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinner.setAdapter(aa);

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
            String[] parsedJson;
            try{
                String jsonStringFromWeb = JsonUtility.getResponseFromSite(strings[0]);
                //Log.d("json raw", jsonStringFromWeb);
               // parsedJson = JsonUtility.formatDetailTrailerJson(jsonStringFromWeb);
                return jsonStringFromWeb;
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String jsonRawString) {//json string sent to onPost so 2 seperate arrays are created, one for reviews and one for trailers
            String[] youtubeKeyArray, nonNonReviewName;
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
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    /*
    public class GetDetailTrailerInfoTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... strings) {    //pass in url   return 2darray of parsed json
            String[] parsedJson;
            try{
                String jsonStringFromWeb = JsonUtility.getResponseFromSite(strings[0]);
                Log.d("json raw", jsonStringFromWeb);
                parsedJson = JsonUtility.formatDetailTrailerJson(jsonStringFromWeb);
                return parsedJson;
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String[] jsonPosterKeyArray) {
            posterKeyArray = jsonPosterKeyArray;
            Log.d("detail post", posterKeyArray[1]);
            //TODO create an adapter based on the number of trailers
        }
    }
    */

}
