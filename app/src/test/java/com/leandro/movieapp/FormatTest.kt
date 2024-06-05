package com.leandro.movieapp

import com.leandro.movieapp.utils.Formats
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.math.BigInteger

class FormatTest {

    @Test
    fun format_isCorrect() {
        val value = 1000
        assertEquals("$1,000.00", Formats.formatCurrency(value))
    }

    @Test
    fun formatString_isCorrect() {
        val value = "1000"
        assertEquals("1000", Formats.formatCurrency(value))
    }

    @Test
    fun formatDouble_isCorrect() {
        val value = 1000.00
        assertEquals("$1,000.00", Formats.formatCurrency(value))
    }

    @Test
    fun formatEmpty_isCorrect(){
        val value = ""
        assertEquals("$0",  Formats.formatCurrency(value))
    }

    @Test
    fun formatNull_isCorrect(){
        val value = null
        assertEquals("$0",  Formats.formatCurrency(value))
    }
}