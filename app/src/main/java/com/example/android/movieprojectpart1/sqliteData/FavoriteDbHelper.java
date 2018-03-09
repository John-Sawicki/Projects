package com.example.android.movieprojectpart1.sqliteData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.movieprojectpart1.sqliteData.FavoriteMovieContract.FavoriteMovieEntry;

public class FavoriteDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;
    public FavoriteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_LIST = "Create TABLE " +
                FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMovieEntry.POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteMovieEntry.ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieEntry.RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMovieEntry.VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavoriteMovieEntry.OVERVIEW + " TEXT NOT NULL, " +
                FavoriteMovieEntry.ID + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);//add new additions to the database
    }
}
