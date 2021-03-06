package com.example.android.movieprojectpart1.sqliteData;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMovieContract  {
    public static final String AUTHORITY = "com.example.android.movieprojectpart1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";    //same as table name
    public static final class FavoriteMovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES).build();
        public static final String TABLE_NAME ="favorites"; //same value as path
        public static final String POSTER_PATH ="posterpath";           //0
        public static final String ORIGINAL_TITLE ="original_title";    //1
        public static final String RELEASE_DATE ="release_date";        //2
        public static final String VOTE_AVERAGE ="vote_average";        //3
        public static final String OVERVIEW ="overview";                //4
        public static final String ID ="id";                            //5
    }
}
