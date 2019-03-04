package com.rhexgomez.itunes.movie.app.source.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A singleton implementation of the API.
 */
object ItunesApi {

    private val client = OkHttpClient.Builder()
        .build()

    /**
     * NOTE FROM DOCS : By default, Retrofit can only deserialize HTTP bodies into
     * OkHttp's  ResponseBody type and it can only accept its RequestBody type for  @Body.
     *
     * In 2.0.0+, you need to explicitly specify you want a Gson converter:
     */
    val api = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build().create(ItunesApiService::class.java)!!

}
