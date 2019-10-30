package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.flixster.DetailActivity;
import com.example.flixster.R;
import com.example.flixster.TrailerActivity;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //inflates a layout XML item_movie.xml and returns holder inside the viewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }


    //populates data into each item_movie through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder: " + position);
        //get the movie at the passed position
        Movie movie = movies.get(position);
        //bind the movie data into the viewHolder
        holder.bind(movie);
    }


    // returns total count of movies in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }



    //returns count the movies/items
    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout container;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        Button playButton;



        public ViewHolder(@Nonnull View itemView) {
            super(itemView);
            container = (RelativeLayout)itemView.findViewById(R.id.container);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            playButton = itemView.findViewById(R.id.playButton);
        }

        //set listener in every movie
        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;

            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
            } else  {
                imageURL = movie.getPosterPath();
            }

            Glide.with(context).load(imageURL)
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder)
                            .transform(new RoundedCornersTransformation(15, 10))
                            .error(R.drawable.imagenotfound)).into(ivPoster);

            if (movie.getRating() > 7.0) {
                playButton.setVisibility(View.VISIBLE);
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, TrailerActivity.class);
                        i.putExtra("movie", Parcels.wrap(movie));
                        context.startActivity(i);
                    }
                });


            } else {
                playButton.setVisibility(View.INVISIBLE);
            }

            // 1. Register click listener on the whole row
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 2. Navigate to a new activity on tap
                    Intent i = new Intent(context, DetailActivity.class);

                    i.putExtra("movie", Parcels.wrap(movie));
//                    Pair<View, String> p1 = Pair.create((View)tvTitle, "title");
//                    Pair<View, String> p2 = Pair.create((View)tvOverview, "overview");
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation((Activity)context, p1, p2);
                    context.startActivity(i);
                }
            });
        }
    }
}

