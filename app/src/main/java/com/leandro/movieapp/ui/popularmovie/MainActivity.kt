package com.leandro.movieapp.ui.popularmovie

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.leandro.movieapp.R
import com.leandro.movieapp.ui.singlemovie.SingleMovieActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, SingleMovieActivity::class.java).also {
            it.putExtra(SingleMovieActivity.ID, 299534)
        })

    }
}

