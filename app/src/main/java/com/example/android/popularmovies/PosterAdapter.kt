package com.example.android.popularmovies

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.android.popularmovies.model.MovieModel
import com.example.android.popularmovies.util.MovieUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.grid_movie_item.view.*


/**
 * Created by msk-1196 on 6/18/17.
 */

class PosterAdapter(private val mClickHandler: PosterAdapterOnClickHandler) :
        RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {
    private var movies: List<MovieModel>? = null

    interface PosterAdapterOnClickHandler {
        fun onClick(movie: MovieModel)
    }

    fun setMovies(movies: List<MovieModel>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        return PosterViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.grid_movie_item, parent, false))
    }

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        holder.bind(movies!![position])
    }

    override fun getItemCount(): Int {
        return if (movies == null)
            0
        else
            movies!!.size
    }

    inner class PosterViewHolder (itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var mPosterImageView: ImageView = itemView.iv_poster

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: MovieModel) {
            if (movie.posterPath != null)
                Picasso.with(itemView.context)
                        .load(MovieUtil.getFullPosterPath(movie.posterPath))
                        .into(mPosterImageView)
        }

        override fun onClick(view: View) {
            mClickHandler.onClick(movies!![adapterPosition])
        }
    }
}
