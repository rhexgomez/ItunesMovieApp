package com.rhexgomez.itunes.movie.app

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * An executor for IO operation like read and writing the Database
 * or a File.
 *
 * Note : never use this for network operation but instead
 * use the [NETWORK_EXECUTOR] executor.
 *
 * This property is Threadsafe!
 */
val IO_EXECUTOR: Executor by lazy {
    Executors.newFixedThreadPool(1)
}

/**
 * An executor for network related operation like consuming the API.
 *
 * This property is Threadsafe!
 */
val NETWORK_EXECUTOR: Executor by lazy {
    Executors.newFixedThreadPool(4)
}

