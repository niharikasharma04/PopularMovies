package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nihar on 1/20/2017.
 */

public class FavouriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION=9;

    public FavouriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " +
                FavouriteContract.FavouritesEntry.TABLE_NAME + "(" +
                FavouriteContract.FavouritesEntry._ID + " INTEGER PRIMARY KEY, " +
                FavouriteContract.FavouritesEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                FavouriteContract.FavouritesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, " +
                FavouriteContract.FavouritesEntry.COLUMN_BACDROP_PATH + " TEXT NOT NULL, " +
                FavouriteContract.FavouritesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                FavouriteContract.FavouritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavouriteContract.FavouritesEntry.COLUMN_USER_RATING + " TEXT NOT NULL, " +
                FavouriteContract.FavouritesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteContract.FavouritesEntry.TABLE_NAME);
        Log.d("after drop","calling on create");
        onCreate(sqLiteDatabase);

    }
}
