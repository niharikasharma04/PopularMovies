package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.popularmovies.data.FavouriteContract.FavouritesEntry.TABLE_NAME;

/**
 * Created by nihar on 1/24/2017.
 */

public class FavouriteContentProvider extends ContentProvider {

    private FavouriteDbHelper mFavouriteDbHelper;
    @Override
    public boolean onCreate() {
        Context context =getContext();
        Log.d("in fav content provider","context="+context);
        mFavouriteDbHelper=new FavouriteDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mFavouriteDbHelper.getReadableDatabase();
        Cursor retCursor;
        retCursor = db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.d("in content provider","in insert");
        Uri returnUri = null;
        final SQLiteDatabase db = mFavouriteDbHelper.getWritableDatabase();
        long id = db.insert(TABLE_NAME,null,contentValues);
        if( id > 0)
        {
            returnUri = ContentUris.withAppendedId(FavouriteContract.FavouritesEntry.CONTENT_URI,id);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mFavouriteDbHelper.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME,selection,selectionArgs);
        if(deletedRows>0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
