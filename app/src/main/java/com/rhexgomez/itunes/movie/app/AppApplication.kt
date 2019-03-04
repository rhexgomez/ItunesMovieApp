package com.rhexgomez.itunes.movie.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * This [Application] sublcass will initialize our app context.
 */
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        globalContext = this
    }

    companion object {

        /**
         * App level context
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var globalContext: Context
    }
}