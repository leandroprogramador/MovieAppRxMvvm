package com.leandro.movieapp.data.vo


import com.google.gson.annotations.SerializedName
import com.leandro.movieapp.utils.Formats
import java.math.BigInteger

data class MovieDetails(
    val budget: BigInteger,
    val id: Int,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("release_date")
    val releaseDate: String,
    val revenue: BigInteger,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,

){
    fun showRating() = rating.toString()
    fun showRuntime() = String.format("%d minutes", runtime)

    fun showBudget() = Formats.formatCurrency(budget)
    fun showRevenue() = Formats.formatCurrency(revenue)
}