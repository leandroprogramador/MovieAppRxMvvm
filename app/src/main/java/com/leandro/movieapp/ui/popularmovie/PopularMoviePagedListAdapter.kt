package com.leandro.movieapp.ui.popularmovie

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leandro.movieapp.R
import com.leandro.movieapp.data.api.POSTER_BASE_URL
import com.leandro.movieapp.data.repository.NetworkState
import com.leandro.movieapp.data.vo.Movie
import com.leandro.movieapp.ui.singlemovie.SingleMovieActivity

class PopularMoviePagedListAdapter(private val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState : NetworkState? = null
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow() : Boolean {
        return networkState!=null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int =
        if(hasExtraRow() && position == itemCount-1) NETWORK_VIEW_TYPE else MOVIE_VIEW_TYPE


    override fun getItemCount(): Int =
        super.getItemCount() + if(hasExtraRow()) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view : View

        if(viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder (view : View) :RecyclerView.ViewHolder(view) {
        val movieTitle = itemView.findViewById<TextView>(R.id.movie_title)
        val movieRelease = itemView.findViewById<TextView>(R.id.movie_release_date)
        val movieCover = itemView.findViewById<ImageView>(R.id.iv_movie_poster)
        fun bind(movie: Movie?, context : Context) {
            movieTitle.text = movie?.title
            movieRelease.text = movie?.releaseDate

            val moviePoster = String.format("%s%s", POSTER_BASE_URL, movie?.posterPath)
            Glide.with(itemView.context)
                .load(moviePoster)
                .into(movieCover)

            itemView.setOnClickListener {
                context.startActivity(Intent(context, SingleMovieActivity::class.java).also {
                    it.putExtra(SingleMovieActivity.ID, movie?.id)
                })
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar_item)
        val textError = itemView.findViewById<TextView>(R.id.error_msg_item)
        fun bind(networkState : NetworkState?) {
            if(networkState != null && networkState == NetworkState.LOADING){
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }

            if(networkState != null && networkState == NetworkState.ERROR){
                textError.visibility = View.VISIBLE
                textError.text = networkState.msg
            } else {
                textError.visibility = View.GONE
            }
        }
    }

    fun setNetworkStateAvailable(newNetworkState: NetworkState) {
        val previousState  = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow) {
            if(hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if(hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount-1)
        }
    }
}