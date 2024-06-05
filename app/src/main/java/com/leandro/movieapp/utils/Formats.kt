package com.leandro.movieapp.utils

import java.math.BigInteger
import java.text.NumberFormat
import java.util.Locale

object Formats {

    fun formatCurrency(value : Any?) : String {
        if(value == null || value == "") return "$0"
        return try {
            val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
            formatCurrency.format(value)
        } catch (ex : Exception) {
            value.toString()
        }
    }
}