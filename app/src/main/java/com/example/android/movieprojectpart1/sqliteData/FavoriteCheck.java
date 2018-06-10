package com.example.android.movieprojectpart1.sqliteData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.movieprojectpart1.sqliteData.FavoriteMovieContract.FavoriteMovieEntry;

public class FavoriteCheck {
    Context mContext;
    boolean isFavorite = false;
    public FavoriteCheck(Context context){
        mContext = context;
    }
    public SQLiteDatabase mDb;
    public  boolean CheckForFavorite(String movieName){ //check if movie is in db to prevent adding it a second time and show yellow star in detail activity
        Log.d("fav input string title",movieName);
        String[] movieNameArray={movieName};
        FavoriteDbHelper dbHelper = new FavoriteDbHelper(mContext);
        mDb =dbHelper.getReadableDatabase();

        Cursor cursor = mDb.query(FavoriteMovieEntry.TABLE_NAME,null,
                FavoriteMovieEntry.ORIGINAL_TITLE+"=?",movieNameArray,
                null, null, null);

        /*
        Cursor cursor = mDb.query(FavoriteMovieEntry.TABLE_NAME,null,
                null,null,
                null, null, null);
                */
        if(cursor.moveToFirst()){
            for(int i = 0; i<cursor.getCount();i++){
                Log.d("fav cursor count", cursor.getCount()+"");
                cursor.moveToPosition(i);
                String cursorMovieTitle = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.ORIGINAL_TITLE));
                Log.d("fav cursor movie title", cursorMovieTitle);

                if(cursorMovieTitle.equals(movieName)) isFavorite = true;
                cursor.moveToNext();
            }
        }
        Log.d("fav isFavorite", isFavorite+"");
        return isFavorite;
    }
}
