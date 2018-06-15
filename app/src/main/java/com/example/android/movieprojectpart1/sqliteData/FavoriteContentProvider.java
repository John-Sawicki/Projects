package com.example.android.movieprojectpart1.sqliteData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.movieprojectpart1.sqliteData.FavoriteMovieContract.FavoriteMovieEntry;

public class FavoriteContentProvider extends ContentProvider{
    private FavoriteDbHelper mFavoriteDbHelper;
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    String[] projectionQuery = {FavoriteMovieEntry.POSTER_PATH};    //detail saves only the values, but we only need the poster path
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher((UriMatcher.NO_MATCH));
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITES,
                MOVIES);    //package, table, Uri identifying int value
        uriMatcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITES
                +"/#",MOVIES_WITH_ID );//probably not going to be used
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteDbHelper = new FavoriteDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;    //not used
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues cv) {
        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        int match =mUriMatcher.match(uri);     //returns 100 or 101
        Uri returnUri;
        switch(match){
            case MOVIES:
                long id= db.insert(FavoriteMovieEntry.TABLE_NAME, null, cv);
                returnUri = ContentUris.withAppendedId(FavoriteMovieEntry.CONTENT_URI, id);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri); //required when using a return value
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;//only using insert
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoriteDbHelper.getReadableDatabase();
        int match =mUriMatcher.match(uri);
        Cursor favCursor;
        Log.d("query CP", "start");
        switch (match){
            case MOVIES:
                favCursor = db.query(FavoriteMovieEntry.TABLE_NAME,
                        null,
                        null,null,
                        null, null, null);
                //Log.d("query CP", "case");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return favCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;//not used
    }
}
