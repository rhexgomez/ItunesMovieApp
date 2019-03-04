package com.rhexgomez.itunes.movie.app.source.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import com.rhexgomez.itunes.movie.app.IO_EXECUTOR
import com.rhexgomez.itunes.movie.app.source.api.ItunesApi
import com.rhexgomez.itunes.movie.app.source.boundary.BoundaryBundle
import com.rhexgomez.itunes.movie.app.source.boundary.MovieBoundary
import com.rhexgomez.itunes.movie.app.source.cache.ItunesMovieCache
import com.rhexgomez.itunes.movie.app.source.cache.MovieEntity
import com.rhexgomez.itunes.movie.app.source.cache.WatchHistoryEntity
import java.util.*

/**
 * Encapsulates the Movie source's implementation (e.g. Network and Cache).
 * Note : It safe to build a singleton instance of this class.
 */
class MovieRepository {
    private val api = ItunesApi.api
    private val db = ItunesMovieCache.database

    /**
     * @return The single source of truth for cached movies. This comes with an observable
     * therefore you can use this to observe for any changes.
     *
     * @param itemCount the number of items to load in the [androidx.recyclerview.widget.RecyclerView]
     *             during first query. This implements a "limit" query internally in Room DB.
     */
    fun getMovies(keyword: String, itemCount: Int = 50): BoundaryBundle<MovieEntity> {
        val movieBoundary = MovieBoundary(keyword, api, db, itemCount)
        val movieFactory = db.movieDao().getMoviesByKeyword(keyword)
        val pageList = LivePagedListBuilder<Int, MovieEntity>(movieFactory, itemCount).apply {
            setBoundaryCallback(movieBoundary)
        }.build()
        return BoundaryBundle(pageList, movieBoundary.itemCountSignal)
    }

    /**
     * @return the movie with an id equal to [movieId]. This function
     * will also store date during when it is last accessed.
     */
    fun getMovie(movieId: String): LiveData<MovieEntity> {
        IO_EXECUTOR.execute {
            db.watchHistoryDao().insert(WatchHistoryEntity(movieId, Date()))
        }
        return db.movieDao().getMovie(movieId)
    }

    fun getWatchHistory() = db.watchHistoryDao().getMovieHistory()
}