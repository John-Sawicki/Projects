package com.example.android.movieprojectpart1.sqliteData;

import android.provider.BaseColumns;

public class FavoriteMovieContract  {
    public static final class FavoriteMovieEntry implements BaseColumns{
        public static final String TABLE_NAME ="favorites";
        public static final String POSTER_PATH ="posterpath";
        public static final String ORIGINAL_TITLE ="original_title";
        public static final String RELEASE_DATE ="release_date";
        public static final String VOTE_AVERAGE ="vote_average";
        public static final String OVERVIEW ="overview";
        public static final String ID ="id";
    }
}
