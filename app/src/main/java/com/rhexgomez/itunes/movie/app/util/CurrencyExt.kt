package com.rhexgomez.itunes.movie.app.util


fun Double.toCurrency(code: String): String {
    return "$this $code"
}