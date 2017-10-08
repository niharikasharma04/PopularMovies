package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.FavouriteContract;
import com.squareup.picasso.Picasso;

/**
 * Created by nihar on 1/25/2017.
 */

public class FavouriteRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public FavouriteRecyclerViewAdapter(Context context,Cursor cursor){
        this.mCursor=cursor;
        this.mContext=context;
    }

    @Override
    public FavouriteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent,false);

        FavouriteRecyclerViewAdapter.ViewHolder vh = new FavouriteRecyclerViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(FavouriteRecyclerViewAdapter.ViewHolder holder, final int position) {

        final MovieData movieData;

        movieData=MovieData.getMovieData(mCursor,position);

        mCursor.moveToPosition(position);

        int poster_index = mCursor.getColumnIndex(FavouriteContract.FavouritesEntry.COLUMN_MOVIE_POSTER_PATH);

        String path= mCursor.getString(poster_index);

        Picasso.with(mContext).load(path).placeholder(R.drawable.in_progress).error(R.drawable.placeholder_error).into(holder.imageViewId);
        holder.imageViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MovieDetails.class);
                intent.putExtra("moviedata",movieData);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        if (mCursor == null) {
            return 0;
        }

        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageViewId;

        public ViewHolder(View itemView) {
            super(itemView);
            imageViewId = (ImageView) itemView.findViewById(R.id.poster);
        }
    }
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
