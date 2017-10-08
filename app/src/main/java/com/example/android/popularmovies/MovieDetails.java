package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.FavouriteContract;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity  {

    @BindView(R.id.detail_overview) TextView synopsis_txt_view;
    @BindView(R.id.detail_image) ImageView poster_image;
    @BindView(R.id.rating) TextView rating_txtView;
    @BindView(R.id.release_date) TextView release_date;
    @BindView(R.id.movie_title) Toolbar toolbar_Title;
    @BindString(R.string.api_key) String api_key;
    @BindString(R.string.detail_url_header) String url_header;
    @BindView(R.id.rv_reviews) RecyclerView rcv_reviews;
    @BindView(R.id.rv_trailer) RecyclerView rcv_trailers;
    private Boolean isFav = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        rcv_reviews.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager_trailer = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        rcv_trailers.setLayoutManager(layoutManager_trailer);
        final FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.addFavourites);
        final MovieData movieData = intent.getParcelableExtra("moviedata");

        Cursor favOrNot = getContentResolver().query(FavouriteContract.FavouritesEntry.CONTENT_URI, null,
                FavouriteContract.FavouritesEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{movieData.movie_id}, null);
        if (favOrNot.moveToFirst()) {
            isFav = true;
            myFab.setColorFilter(getResources().getColor(R.color.test1));
        }
        favOrNot.close();


        if(movieData != null){

        Picasso.with(getApplicationContext()).load(movieData.background_path).placeholder(R.drawable.in_progress).error(R.drawable.placeholder_error).into(poster_image);

        synopsis_txt_view.setText(movieData.synopsis);
        release_date.setText("Year  "+movieData.release_date);
        rating_txtView.setText("Rating  "+movieData.user_Rating+ "/10");
        toolbar_Title.setTitle(movieData.title);}

        String review_url = url_header+movieData.movie_id+"/reviews?api_key="+api_key;
        String trailer_url = url_header+movieData.movie_id+"/videos?api_key="+api_key;

        GetReviews getReviews = new GetReviews(review_url,rcv_reviews);
        getReviews.execute();
        GetTrailers getTrailers = new GetTrailers(trailer_url,rcv_trailers,this);
        getTrailers.execute();


        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               Cursor favOrNot = getContentResolver().query(FavouriteContract.FavouritesEntry.CONTENT_URI, null,
                       FavouriteContract.FavouritesEntry.COLUMN_MOVIE_ID + " = ? ",
                        new String[]{movieData.movie_id}, null);
                if (favOrNot.moveToFirst()) {


                    myFab.clearColorFilter();
                    myFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fabRemovedBackground)));
                    myFab.setColorFilter(getResources().getColor(R.color.fabRemovedIcon));
                    int deletedRows = getContentResolver().delete(FavouriteContract.FavouritesEntry.CONTENT_URI,
                            FavouriteContract.FavouritesEntry.COLUMN_MOVIE_ID + " = ? ",
                            new String[]{movieData.movie_id});
                    Toast.makeText(getApplicationContext(),"Removed from favourites",Toast.LENGTH_SHORT).show();
                } else {

                    myFab.setColorFilter(getResources().getColor(R.color.test1));

                    String input = movieData.title;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_ID, movieData.movie_id);
                    contentValues.put(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_NAME, input);
                    contentValues.put(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_POSTER_PATH, movieData.poster_path);
                    contentValues.put(FavouriteContract.FavouritesEntry.COLUMN_BACDROP_PATH, movieData.background_path);
                    contentValues.put(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_OVERVIEW, movieData.synopsis);
                    contentValues.put(FavouriteContract.FavouritesEntry.COLUMN_RELEASE_DATE, movieData.release_date);
                    contentValues.put(FavouriteContract.FavouritesEntry.COLUMN_USER_RATING, movieData.user_Rating);
                    Uri uri = getContentResolver().insert(FavouriteContract.FavouritesEntry.CONTENT_URI, contentValues);
                    Toast.makeText(getApplicationContext(),"Added to favourites",Toast.LENGTH_SHORT).show();
                }

                //finish();
            }
        });

    }

}
