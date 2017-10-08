package com.example.android.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.popularmovies.data.FavouriteContract;

import static com.example.android.popularmovies.R.menu.spinner_menu;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG= MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    private Spinner spinner;
    private String stringUrl;
    private int selection;
    private FavouriteRecyclerViewAdapter movieAdapter;
    private boolean isFav = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState !=null){
         selection =savedInstanceState.getInt("saved_selection");
        }
        stringUrl=this.getString(R.string.pop_url);
         recyclerView = (RecyclerView)findViewById(R.id.rv_movies);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

////FavLoader fav = new FavLoader(this);

//Log.d("in main activity","after get support");


    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        if(spinner != null){
       savedInstanceState.putInt("saved_selection",spinner.getSelectedItemPosition());
        super.onSaveInstanceState(savedInstanceState);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(spinner_menu,menu);
        MenuItem item=menu.findItem(R.id.spinner);
         spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(selection);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String api_key=getApplicationContext().getString(R.string.api_key).toString();



                if(spinner.getSelectedItem().toString().equalsIgnoreCase("Highest Rated"))
                { String top_url=getApplicationContext().getString(R.string.top_rated_url).toString();
                    stringUrl=top_url+api_key;
                    GetMovies getMovies=new GetMovies(stringUrl,MainActivity.this,recyclerView);
                getMovies.execute();
                    //recyclerView.setAdapter(movieAdapter);
                    }
                else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Most Popular")){
                    String pop_url=getApplicationContext().getString(R.string.pop_url).toString();
                    stringUrl=pop_url+api_key;
                    GetMovies getMovies=new GetMovies(stringUrl,MainActivity.this,recyclerView);
                    getMovies.execute();
                    //recyclerView.setAdapter(movieAdapter);
                    }
                else if (spinner.getSelectedItem().toString().equalsIgnoreCase("Favourites")){
                    isFav = true;
                    getSupportLoaderManager().initLoader(0, null, MainActivity.this);

                    Log.d("on item selected","after set");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("on nothing selected","here");
            }
        });
        return true;
    }
    @Override
    protected void onResume(){
        Log.d("in on resume","restarting loader");
        Log.d("in resume","fav="+isFav);
       super.onResume();
        if (isFav)
        getSupportLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("in on crwate loader","");
        return new CursorLoader(this)  {

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
                Log.d("in cursor loadin bckgrd","");
                try {
                    Log.d("in cursor loadin bckgrd","in try");
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
                Log.d("in deliver result","");
                mTaskData = data;
                super.deliverResult(data);
            }




        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("on load finished","in fnsh");
       movieAdapter =new FavouriteRecyclerViewAdapter(MainActivity.this,data);
           // movieAdapter.setCursor(data);

        Log.d("after set cusr","set adptr");
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.swapCursor(data);



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
Log.d("in loader reset","");
    }


}
