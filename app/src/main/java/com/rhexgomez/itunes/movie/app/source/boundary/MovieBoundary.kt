package com.rhexgomez.itunes.movie.app.source.boundary

import androidx.core.net.toUri
import androidx.paging.PagedList
import com.rhexgomez.itunes.movie.app.IO_EXECUTOR
import com.rhexgomez.itunes.movie.app.NETWORK_EXECUTOR
import com.rhexgomez.itunes.movie.app.source.api.ItunesApiService
import com.rhexgomez.itunes.movie.app.source.api.Movie
import com.rhexgomez.itunes.movie.app.source.boundary.PagingRequestHelper.RequestType.AFTER
import com.rhexgomez.itunes.movie.app.source.boundary.PagingRequestHelper.RequestType.INITIAL
import com.rhexgomez.itunes.movie.app.source.cache.ItunesMovieCache
import com.rhexgomez.itunes.movie.app.source.cache.MovieEntity
import com.rhexgomez.itunes.movie.app.source.cache.SearchResultsEntity
import com.rhexgomez.itunes.movie.app.util.toDate
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * Notifies if we reached the last Movie in our List or no item is present in our database.
 * This is a good place to trigger a network operation and cache the results.
 *
 * Note : Threading must be handled explicitly!
 */
class MovieBoundary(
    private val searchKeyword: String,
    private val api: ItunesApiService,
    private val db: ItunesMovieCache,
    private val downloadCount: Int
) : PagedList.BoundaryCallback<MovieEntity>() {

    private var itemCount = 0
    private val movieDao = db.movieDao()
    val helper = PagingRequestHelper(IO_EXECUTOR)

    /**
     * Notifies the boundary whenever the item count changes. We need
     * to explicitly call it within the view.
     */
    val itemCountSignal: (Int) -> Unit = {
        itemCount = it
    }

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(INITIAL) {
            NETWORK_EXECUTOR.execute {
                api.searchMoviesFromAustralia(
                    keyword = searchKeyword,
                    limit = downloadCount
                ).callbackSuccess(it) {
                    db.runInTransaction {
                        val movieEntities = it.body()?.movies?.map(Movie::toEntity)
                        movieEntities?.run { movieDao.insertMovies(this) }
                        val searchEntities = movieEntities?.map { SearchResultsEntity(searchKeyword, it.id) }
                        searchEntities?.run { movieDao.insertSearches(this) }
                    }
                }
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: MovieEntity) {
        helper.runIfNotRunning(AFTER) {
            NETWORK_EXECUTOR.execute {
                api.searchMoviesFromAustralia(
                    keyword = searchKeyword,
                    limit = downloadCount,
                    offset = itemCount.toOffset(downloadCount)
                ).callbackSuccess(it) {
                    db.runInTransaction {
                        val movieEntities = it.body()?.movies?.map(Movie::toEntity)
                        movieEntities?.run { movieDao.insertMovies(this) }
                        val searchEntities = movieEntities?.map { SearchResultsEntity(searchKeyword, it.id) }
                        searchEntities?.run { movieDao.insertSearches(this) }
                    }
                }
            }
        }
    }
}

/**
 * Handles the exception when doing network call!
 */
private inline fun <T> Call<T>.callbackSuccess(callback: PagingRequestHelper.Callback, func: (Response<T>) -> Unit) {
    try {
        val response = execute()
        if (response.isSuccessful) {
            func(response)
            callback.recordSuccess()
        } else {
            callback.recordFailure(HttpException(response))
        }
    } catch (ex: IOException) {

        callback.recordFailure(ex)
    }
}

/**
 * Convert [Movie] to [MovieEntity].
 */
private fun Movie.toEntity() = MovieEntity(
    trackId.toString(),
    trackName,
    trackPrice ?: 0.0,
    currency,
    shortDescription,
    longDescription,
    releaseDate!!.toDate(),
    primaryGenreName!!,
    artistName!!,
    previewUrl?.toUri(),
    artworkUrl100?.replace("100x100", "600x600")?.toUri() // we need high res image
)

/**
 * Convert the total item count to offset base on limit.
 */
private fun Int.toOffset(limit: Int) = if (this % limit == 0) {
    this / limit
} else {
    (this / limit) + 1
}
