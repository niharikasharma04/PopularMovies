package com.example.android.popularmovies;

/**
 * Created by nihar on 1/28/2017.
 */

public class Trailers {

    public String key;
    public String img_path;

    public Trailers(String key) {
        this.key = key ;
        this.img_path = "https://img.youtube.com/vi/" + key+ "/default.jpg";

    }
}
