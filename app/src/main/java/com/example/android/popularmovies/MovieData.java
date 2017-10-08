package com.example.android.popularmovies;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmovies.data.FavouriteContract;

/**
 * Created by nihar on 1/6/2017.
 */
public class MovieData implements Parcelable {

    public String poster_path;
    public String synopsis;
    public String title;
    public String user_Rating;
    public String background_path;
    public String release_date;
    public String movie_id;

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }


    public MovieData(String movie_id,String movie_poster, String synopsis, String title, String user_Rating, String background_path, String release_date) {
        this.movie_id = movie_id;
        this.poster_path = "http://image.tmdb.org/t/p/w500/" + movie_poster;
        this.synopsis = synopsis;
        this.title = title;
        this.user_Rating = user_Rating;
        if(!(background_path.contains("http://image.tmdb.org/t/p/w500/"))){
            this.background_path = "http://image.tmdb.org/t/p/w500/" + background_path;

        }
        else
        {
            this.background_path = background_path;
        }


        this.release_date = release_date.substring(0, 4);

    }

    protected MovieData(Parcel in) {
        movie_id = in.readString();
        poster_path = in.readString();
        synopsis = in.readString();
        title = in.readString();
        user_Rating = in.readString();
        background_path = in.readString();
        release_date = in.readString();
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movie_id);
        parcel.writeString(poster_path);
        parcel.writeString(synopsis);
        parcel.writeString(title);
        parcel.writeString(user_Rating);
        parcel.writeString(background_path);
        parcel.writeString(release_date);
    }

    public static MovieData getMovieData(Cursor cursor, int position) {
        if (cursor.moveToFirst()) {
            cursor.moveToPosition(position);

            return new MovieData(cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_NAME)),
                    cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouritesEntry.COLUMN_USER_RATING)),
                    cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouritesEntry.COLUMN_BACDROP_PATH)),
                    cursor.getString(cursor.getColumnIndex(FavouriteContract.FavouritesEntry.COLUMN_RELEASE_DATE)));
        }
        return null;
    }
}
