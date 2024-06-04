package com.leandro.movieapp.ui.singlemovie

import androidx.lifecycle.LiveData
import com.leandro.movieapp.data.api.TheMovieDBInterface
import com.leandro.movieapp.data.repository.MovieDetailsNetworkDataSource
import com.leandro.movieapp.data.repository.NetworkState
import com.leandro.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService : TheMovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetail(movieId)
        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse
    }

    fun getMovieDetailsNetworkState() : LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}