package com.example.android.movieprojectpart1.utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JsonUtility {
    public static String getResponseFromSite(String stringUrl)throws IOException{
        Uri builtUri = Uri.parse(stringUrl);
        URL url = new URL(builtUri.toString());
        HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                Log.d("response from site", "url has input");
                return scanner.next();  //returns a string if it works
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public static String[][] formatJson(String rawJSON) throws JSONException{
        String mRawJSON = rawJSON;
        String formattedJson[][]= new String[20][5];
        try{
            JSONObject movieJson = new JSONObject(mRawJSON);
            JSONArray movieArray = movieJson.getJSONArray("results");
            for(int i=0; i<20;i++){ //loop through all 20 movies in json data
                JSONObject aMovie = movieArray.getJSONObject(i);
                formattedJson[i][0] = aMovie.getString("poster_path");
                formattedJson[i][1] = aMovie.getString("original_title");
                formattedJson[i][2] = aMovie.getString("release_date");
                formattedJson[i][3] = ""+aMovie.getDouble("vote_average");
                formattedJson[i][4] = aMovie.getString("overview");
            }
            Log.d("json",formattedJson[0][0]);
            Log.d("json",formattedJson[0][1]);
            Log.d("json",formattedJson[0][2]);
            Log.d("json",formattedJson[0][3]);
            Log.d("json",formattedJson[0][4]);
            return formattedJson;
        }catch(Exception e){
            e.printStackTrace(); return null;
        }

    }
}
