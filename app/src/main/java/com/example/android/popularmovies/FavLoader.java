package com.example.android.popularmovies;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.popularmovies.data.FavouriteContract;

/**
 * Created by nihar on 1/24/2017.
 */

public class FavLoader extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    protected Context context;
    public FavLoader(Context context){
        this.context= context.getApplicationContext();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.d("in on create loader","");

        return new AsyncTaskLoader<Cursor>(context)  {

            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                Log.d("on start load","");
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(FavouriteContract.FavouritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            FavouriteContract.FavouritesEntry.COLUMN_MOVIE_NAME);

                } catch (Exception e) {
                    Log.e("load in background", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }

            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }




        };
    }



    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
