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
    public static String[] formatJson(String rawJSON) throws JSONException{
        String mRawJSON = rawJSON;
        String formattedJson[]= new String[5];
        try{
            JSONObject movieJson = new JSONObject(mRawJSON);
            JSONArray movieArray = movieJson.getJSONArray("results");
            for(int i=0; i<1;i++){
                JSONObject aMovie = movieArray.getJSONObject(i);
                formattedJson[0] = aMovie.getString("poster_path"); Log.d("json",formattedJson[0]);
                formattedJson[1] = aMovie.getString("original_title");Log.d("json",formattedJson[1]);
                formattedJson[2] = aMovie.getString("release_date");Log.d("json",formattedJson[2]);
                formattedJson[3] = ""+aMovie.getDouble("vote_average");Log.d("json",formattedJson[3]);
                formattedJson[4] = aMovie.getString("overview");Log.d("json",formattedJson[4]);
            }

            return null;
        }catch(Exception e){
            e.printStackTrace(); return null;
        }

    }
}
