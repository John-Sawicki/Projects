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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieprojectpart1.utilities.API;
import com.example.android.movieprojectpart1.utilities.JsonUtility;
import com.example.android.movieprojectpart1.utilities.ReviewAdapter;
import com.example.android.movieprojectpart1.utilities.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TAOnClickHandler{
    private TextView titleText, releaseDateText, userRatingText, overviewText;
    private ImageView moviePosterImage;
    private String[] movieData, posterKeyArray;
    private String [][] movieReviewArray;
    private String movieId = ""+157336;
    private String movieDetailUrl, youTubeUrl="https://www.youtube.com/watch?v=";   //base url with no key
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mTrailerRV, mReviewerRV;
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

        }



    }

    @Override
    public void onClick(String trailerName) {
        Context context = this;
        //Toast.makeText(context, trailerName, Toast.LENGTH_SHORT).show();
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
                movieReviewArray = reviewArray;
                for(int i=0; i<10;i++){
                    if(movieReviewArray[i][0]!=null){   //avoid null pointer if there are less than 10 reviews from json, starts at 0 and increase till null continuously
                        //reviewerAdapter.add(movieReviewArray[i][0]);    //add reviewer name to the array
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            Log.d("detail post", posterKeyArray[1]);
            //TODO create an adapter based on the number of trailers
            //TODO create an adapter based on the number of reviews
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
