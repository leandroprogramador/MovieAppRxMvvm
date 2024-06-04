package com.leandro.movieapp.ui.singlemovie

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.leandro.movieapp.R
import com.leandro.movieapp.data.api.POSTER_BASE_URL
import com.leandro.movieapp.data.api.TheMovieDBClient
import com.leandro.movieapp.data.api.TheMovieDBInterface
import com.leandro.movieapp.data.repository.NetworkState
import com.leandro.movieapp.data.vo.MovieDetails
import com.leandro.movieapp.databinding.ActivitySingleMovieBinding

class SingleMovieActivity : AppCompatActivity() {

    companion object {
        const val ID = "ID"
    }
    private lateinit var viewModel : SingleMovieViewModel
    private lateinit var movieDetailsRepository: MovieDetailsRepository
    val movieId : Int by lazy { intent.getIntExtra(ID, 1) }
    val binding : ActivitySingleMovieBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_single_movie) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieDetailsRepository = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })
        viewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.INVISIBLE
            binding.txtError.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.INVISIBLE
        })
    }

    private fun bindUI(it: MovieDetails?) {
        binding.content.visibility = View.VISIBLE
        binding.movie = it
        Glide.with(this)
            .load(String.format("%s%s", POSTER_BASE_URL, it!!.posterPath))
            .into(binding.ivMoviePoster)

    }

    private fun getViewModel(movieId: Int) : SingleMovieViewModel {
        return ViewModelProvider.create(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(movieDetailsRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}