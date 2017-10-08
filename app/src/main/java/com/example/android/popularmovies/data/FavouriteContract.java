package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by nihar on 1/20/2017.
 */

public class FavouriteContract {

    public static final String AUTHORITY= "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVOURITES ="favourites";

    public static final class FavouritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();
        public static final String TABLE_NAME= "favourites";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_POSTER_PATH = "posterPath";
        public static final String COLUMN_MOVIE_OVERVIEW = "synopsis";
        public static final String COLUMN_USER_RATING = "userRating";
        public static final String COLUMN_BACDROP_PATH = "backdropPath";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
    }
}
