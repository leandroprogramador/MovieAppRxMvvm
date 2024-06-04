package com.leandro.movieapp.data.api

import com.leandro.movieapp.data.vo.MovieDetails
import com.leandro.movieapp.data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page : Int) : Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id : Int
    ) : Single<MovieDetails>
}