package com.example.android.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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
import java.util.List;

/**
 * Created by nihar on 1/27/2017.
 */
class GetMovies extends AsyncTask<Object, Object, List<MovieData>> {

    private Context context;
    private String stringUrl;
    private List<MovieData> moviesList;
    private RecyclerView recyclerView;

    GetMovies(String url,Context context,RecyclerView recyclerView){
       this.stringUrl = url ;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    protected List<MovieData> doInBackground(Object... arg0) {
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";

        URL url = createUrl(stringUrl);

        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            // TODO Handle the IOException
        }
        moviesList = extractFeatureFromJson(jsonResponse);

        return moviesList;

    }

    @Override
    protected void onPostExecute(List<MovieData> moviedata) {
        if (moviedata == null) {
            return;
        }

        MovieAdapter adapter = new MovieAdapter(context, moviesList);
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onPreExecute() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null)
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        else {
            boolean connected = netInfo.isConnectedOrConnecting();
            if (connected == false)
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
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

    private List<MovieData> extractFeatureFromJson(String movieJSON) {
        try {
            JSONObject baseJsonResponse = new JSONObject(movieJSON);
            JSONArray resultsArray = baseJsonResponse.getJSONArray("results");
            moviesList = new ArrayList<>();

            // If there are results in the results array
            if (resultsArray.length() > 0) {
                for (int i = 0; i < resultsArray.length(); i++) {
                    // Extract out the first feature (which is an earthquake)
                    JSONObject firstFeature = resultsArray.getJSONObject(i);


                    String poster = firstFeature.getString("poster_path");
                    String synopsis = firstFeature.getString("overview");
                    String title = firstFeature.getString("original_title");
                    String user_Rating = firstFeature.getString("vote_average");
                    String backdrop_path = firstFeature.getString("backdrop_path");
                    String released = firstFeature.getString("release_date");
                    String movie_id = firstFeature.getString("id");

                    MovieData mv = new MovieData(movie_id, poster, synopsis, title, user_Rating, backdrop_path, released);
                    mv.setSynopsis(synopsis);
                    moviesList.add(mv);

                }
                return moviesList;
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
