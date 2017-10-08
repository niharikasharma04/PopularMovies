package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nihar on 1/27/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Reviews> reviewsArrayList;


    public ReviewAdapter(ArrayList<Reviews> reviewsList)
    {
        this.reviewsArrayList = reviewsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       public final TextView author_textView;
        public final TextView content_textView;


        public ViewHolder(View v) {
            super(v);

            author_textView = (TextView) v.findViewById(R.id.reviewAuthor);
            content_textView = (TextView) v.findViewById(R.id.reviewContent);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews, parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       final Reviews reviews = reviewsArrayList.get(position);
        if (reviewsArrayList != null){
            final Reviews reviewdata = reviewsArrayList.get(position);
            holder.author_textView.setText(reviews.author);
            holder.content_textView.setText(reviews.content);
        }
    }


    @Override
    public int getItemCount() {

        return reviewsArrayList.size();
    }
}
