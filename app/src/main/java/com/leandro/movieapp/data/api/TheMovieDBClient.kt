package com.leandro.movieapp.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjYjVkNTgyZTNiMjA5YjY2ZTc5ZGFiYjkxZTE2MzdhNCIsInN1YiI6IjY2NWU5MTZlN2IyMTUyMDZlODU0ODk3ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.C0Anyd0mgHxtM_0VzjJRChaPlAVXu1zkrYoNtBfNvdA"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/w342"
object TheMovieDBClient {

    private val requestInterceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", API_KEY)
            .build()

        return@Interceptor chain.proceed(request)
    }

    fun getClient() : TheMovieDBInterface {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)



    }
}