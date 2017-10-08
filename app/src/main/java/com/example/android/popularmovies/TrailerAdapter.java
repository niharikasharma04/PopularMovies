package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nihar on 1/28/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailers> trailersList ;
    private Context context;

    public TrailerAdapter(List<Trailers> trailersList, Context context){
        this.trailersList = trailersList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView image_thumbnail;
        //public final TextView content_textView;


        public ViewHolder(View v) {
            super(v);

            image_thumbnail = (ImageView) v.findViewById(R.id.trailer_thumbnail);


        }
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_trailers, parent,false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {

        Trailers trailers = trailersList.get(position);

         final String urlv = "https://www.youtube.com/watch?v="+trailers.key;

        Picasso.with(context).load(trailers.img_path).placeholder(R.drawable.in_progress).error(R.drawable.placeholder_error).into(holder.image_thumbnail);
        holder.image_thumbnail.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlv)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }
}
