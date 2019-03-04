package com.rhexgomez.itunes.movie.app.source.api

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * A set of API for iTune.
 */
interface ItunesApiService {

    /**
     * Returns all the Movie from Australia that matches the [keyword] keyword.
     *
     * @param keyword the name of the movie to search.
     * @param limit the number of items to download.
     */
    @POST("search?media=movie&country=au")
    fun searchMoviesFromAustralia(
        @Query("term") keyword: String,
        @Query("limit") limit: Int
    ): Call<ItuneResponse>

    /**
     * Returns all the Movie from Australia that matches the [keyword] keyword.
     * This is suitable for Pagination.
     *
     * @param keyword the name of the movie to search.
     * @param limit the number of items to download.
     * @param offset the next batch of download.
     *
     * @see searchMoviesFromAustralia
     */
    @POST("search?media=movie&country=au")
    fun searchMoviesFromAustralia(
        @Query("term") keyword: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<ItuneResponse>
}