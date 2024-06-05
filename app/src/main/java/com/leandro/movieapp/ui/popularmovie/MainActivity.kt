package com.leandro.movieapp.ui.popularmovie

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.leandro.movieapp.R
import com.leandro.movieapp.data.api.TheMovieDBClient
import com.leandro.movieapp.data.api.TheMovieDBInterface
import com.leandro.movieapp.data.repository.NetworkState
import com.leandro.movieapp.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {
    val binding : ActivityMainBinding by lazy { DataBindingUtil.setContentView(this,R.layout.activity_main) }
    private lateinit var viewModel: MainActivityViewModel
    lateinit var moviePageListRepository: MoviePageListRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        moviePageListRepository = MoviePageListRepository(apiService)

        viewModel = getViewModel()
        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if(viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1 else return 3
            }
        }
        binding.recyclerMovies.layoutManager = gridLayoutManager
        binding.recyclerMovies.setHasFixedSize(true)
        binding.recyclerMovies.adapter = movieAdapter

        viewModel.moviePageList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            binding.progressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.txtError.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                binding.content.visibility = View.VISIBLE
                movieAdapter.setNetworkStateAvailable(it)
            }
        })

    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProvider.create(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainActivityViewModel(moviePageListRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}

