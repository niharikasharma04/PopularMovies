package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nihar on 1/6/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;
    private List<MovieData> moviesList;

    public MovieAdapter()
    {

    }
    public MovieAdapter(Context context, List<MovieData> moviesList){

        this.context=context;
        this.moviesList=moviesList;
    }
    public void setCursor(Cursor cursor) {
        this.cursor= cursor;
        Log.d("in movie adaptr","in set cusr");
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (moviesList != null) {
            final MovieData movieData = moviesList.get(position);

            holder.imageViewId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MovieDetails.class);

                    intent.putExtra("moviedata", movieData);
                   // intent.putExtra("isFav","Fav");
                    context.startActivity(intent);
                }
            });
            Picasso.with(context).load(movieData.poster_path).placeholder(R.drawable.in_progress).error(R.drawable.placeholder_error).into(holder.imageViewId);
        }
    }

    @Override
    public int getItemCount() {


        return moviesList.size();

    }



    public class ViewHolder extends RecyclerView.ViewHolder {
       protected  ImageView imageViewId;
        public ViewHolder(View v) {
            super(v);
             imageViewId = (ImageView) v.findViewById(R.id.poster);
        }
    }

        @Override
        public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent,false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

}
