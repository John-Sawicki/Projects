package com.example.android.movieprojectpart1;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.android.movieprojectpart1.utilities.API;
import com.example.android.movieprojectpart1.utilities.JsonUtility;
import com.example.android.movieprojectpart1.utilities.MovieAdapter;

public class MainActivity extends AppCompatActivity implements MovieAdapter.GridItemClickListener{
    private String[][] moviesInfo = new String[20][6];   //20 search results and 6 data points per movie
    private String[] movieInfo = new String [6];//test for 1 movie
    private String moviddbUrl= "https://api.themoviedb.org/3/movie/popular?api_key="  + API.key
            +"&language=en-US   &page=1";
    private TextView errorMessage;
    private String[] moviePosterUrls = new String[20];
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);//vertical by default
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(20, this);
        mRecyclerView.setAdapter(mMovieAdapter);
        new GetMovieInfoTask().execute(moviddbUrl);
        //poster = findViewById(R.id.singlePosterImage); replaced image with recycler view
        errorMessage = findViewById(R.id.errorText);
        errorMessage.setVisibility(View.INVISIBLE);     //only shows if there is am error retrieving json data
        /*
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("detail data",moviesInfo);
                startActivity(intent);
            }
        });
         */
    }
    public class GetMovieInfoTask extends AsyncTask<String, Void, String[][]>{
        @Override
        protected String[][] doInBackground(String... strings) {    //pass in url   return 2darray of parsed json
            String[][] parsedJson = new String[20][5];
            try{
                String jsonStringFromWeb = JsonUtility.getResponseFromSite(strings[0]);
                Log.d("json raw", jsonStringFromWeb);
                parsedJson = JsonUtility.formatJson(jsonStringFromWeb);
                return parsedJson;
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String[][] strings) {
            String imageBaseURL = "https://image.tmdb.org/t/p/w185";
            String fullURL = "";
            if(strings!=null){
                moviesInfo = strings; //2d array with detail info for all 20 movies
                for(int i = 0;i<20; i++){
                    moviePosterUrls[i]= imageBaseURL+moviesInfo[i][0];   //movie url suffix is in column 0
                    //Log.d("onPost", i+" "+moviePosterUrls[i]);
                }
                mMovieAdapter.setUrlData(moviePosterUrls);
                Log.d("onPost", moviesInfo[0][0]);  //copy the async result to the variable on the UI thread to send to detail activity
                Log.d("onPost", imageBaseURL+moviesInfo[0][0]);
                /*
                fullURL = imageBaseURL+moviesInfo[0][0]; Log.d("onPost", fullURL);
                //Picasso.with(getApplicationContext()).load(imageBaseURL+movieInfo[0]).into(poster);
                for(int i=0; i<20;i++){
                    Picasso.with(getApplicationContext())
                            .load(imageBaseURL+moviesInfo[0][0])
                            .into(poster);
                }
                */
                mRecyclerView.setVisibility(View.VISIBLE);
                errorMessage.setVisibility(View.INVISIBLE);
            }else{
                mRecyclerView.setVisibility(View.INVISIBLE);
                errorMessage.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemClicked = item.getItemId();
        if(menuItemClicked==R.id.menu_popular){
            moviddbUrl = "https://api.themoviedb.org/3/movie/popular?api_key="  + API.key +"&language=en-US&page=1";
            Log.d("menu", "popular menu clicked");
            new GetMovieInfoTask().execute(moviddbUrl);
        }
        if(menuItemClicked==R.id.menu_rating){
            moviddbUrl="https://api.themoviedb.org/3/movie/top_rated?api_key="+   API.key +"&language=en-US&page=1";
            Log.d("menu", "rating menu clicked");
            new GetMovieInfoTask().execute(moviddbUrl);
        }
        if(menuItemClicked==R.id.favorites){
            //TODO update grid with values from
        }
        return true;
    }
    @Override
    public void onGridItemClick(int index) {
        Log.d("listener", ""+index);
        for(int i=0; i<6;i++){
            movieInfo[i] = moviesInfo[index][i];    //transfer movie data based on the index clicked
        }
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("detail data",movieInfo);
        startActivity(intent);

    }
}
