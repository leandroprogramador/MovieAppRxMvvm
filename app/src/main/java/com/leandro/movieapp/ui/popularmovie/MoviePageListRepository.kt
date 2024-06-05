package com.leandro.movieapp.ui.popularmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.leandro.movieapp.data.api.POST_PER_PAGE
import com.leandro.movieapp.data.api.TheMovieDBInterface
import com.leandro.movieapp.data.repository.MovieDataSource
import com.leandro.movieapp.data.repository.MovieDataSourceFactory
import com.leandro.movieapp.data.repository.NetworkState
import com.leandro.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository(private val apiService : TheMovieDBInterface) {

    lateinit var moviePageList : LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePageList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePageList
    }

    fun getNetworkState() : LiveData<NetworkState>{
       return movieDataSourceFactory.movieLiveDataSource.switchMap { it.networkState }
    }
}