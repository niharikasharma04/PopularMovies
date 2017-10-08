package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by nihar on 1/27/2017.
 */

public class GetReviews extends AsyncTask<Void,Void,ArrayList<Reviews>> {

    private ArrayList<Reviews> reviewsList;
    private String stringUrl;
    private RecyclerView recyclerView;
    //private Context context;

    GetReviews(String url,RecyclerView recyclerView){
        this.stringUrl = url;
        //this.context = context ;
        this.recyclerView = recyclerView ;
    }
    @Override
    protected ArrayList<Reviews> doInBackground(Void... voids) {
        String jsonResponse = "";

        URL url = createUrl(stringUrl);

        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            // TODO Handle the IOException
        }
        reviewsList = extractFeatureFromJson(jsonResponse);

        return reviewsList;

    }

    @Override
    protected void onPostExecute(ArrayList<Reviews> reviews) {
        if (reviews == null) {
            return;
        }

        ReviewAdapter adapter = new ReviewAdapter( reviewsList);
        recyclerView.setAdapter(adapter);


    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private ArrayList<Reviews> extractFeatureFromJson(String movieJSON) {
        try {
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
            reviewsList = new ArrayList<>();

            // If there are results in the results array
            if (resultsArray.length() > 0) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    // Extract out the first feature (which is an earthquake)
                    JSONObject firstFeature = resultsArray.getJSONObject(i);



                    String author = firstFeature.getString("author");
                    String content = firstFeature.getString("content");
                   // Log.d("in json parse", "movie id=" + movie_id);
                   // MovieData mv = new MovieData(movie_id, poster, synopsis, title, user_Rating, backdrop_path, released);
                   // mv.setSynopsis(synopsis);
                    //reviewsList.add(mv);
                    Reviews rv = new Reviews(author,content);
                    reviewsList.add(rv);
                }
                return reviewsList;
            }
        } catch (JSONException e) {
            Log.e(MainActivity.LOG_TAG, "Problem parsing the movie JSON results", e);
        }
        return null;
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(MainActivity.LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

}
