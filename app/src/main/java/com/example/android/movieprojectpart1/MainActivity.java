package com.example.android.movieprojectpart1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.movieprojectpart1.utilities.JsonUtility;

public class MainActivity extends AppCompatActivity {
    String searchMethod;
    String[][] moviesInfo = new String[20][5];   //20 search results and 5 data points per movie
    String[] movieInfo = new String [5];//test for 1 movie
    String moviddbUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviddbUrl = "https://api.themoviedb.org/3/movie/popular?api_key=aee0191cd58fbf42dd0218a905b434eb&language=en-US&page=1";
        new GetMovieInfoTask().execute(moviddbUrl);
    }
    public class GetMovieInfoTask extends AsyncTask<String, Void, String[]>{
        @Override
        protected String[] doInBackground(String... strings) {
            try{
                String jsonStringFromWeb = JsonUtility.getResponseFromSite(strings[0]);
                Log.d("json raw", jsonStringFromWeb);

            }catch(Exception e){
                e.printStackTrace();
            }
            return new String[0];
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
            searchMethod="popular"; Log.d("menu", "popular menu clicked");
        }
        if(menuItemClicked==R.id.menu_rating){
            searchMethod="top_rated"; Log.d("menu", "rating menu clicked");
        }
        return true;
    }
}
