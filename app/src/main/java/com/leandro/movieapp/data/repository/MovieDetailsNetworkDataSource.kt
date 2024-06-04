package com.leandro.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leandro.movieapp.data.api.TheMovieDBInterface
import com.leandro.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource(private val apiService : TheMovieDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState : LiveData<NetworkState>
        get() = _networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieDetailsResponse : MutableLiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetail(movieId : Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {ex->
                            Log.e("MovieDetailDataSource", ex.message!!)
                            _networkState.postValue(NetworkState.ERROR)
                        }
                    )
            )
        } catch (ex : Exception) {
            Log.e("MovieDetailDataSource", ex.message!!)
            _networkState.postValue(NetworkState.ERROR)
        }
    }


}