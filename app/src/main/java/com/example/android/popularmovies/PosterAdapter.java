package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by msk-1196 on 6/18/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {
    private List<MovieModel> movies;

    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
        notifyDataSetChanged();
        Log.e(this.getClass().getName(), ""+this.getItemCount());
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PosterViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_movie_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        else
            return movies.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_poster) ImageView mPosterImageView;

        private PosterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(MovieModel movie){
            if (movie.getPosterPath() != null)
                Picasso.with(itemView.getContext())
                        .load(movie.getPosterPath())
                        .into(mPosterImageView);
        }
    }
}
