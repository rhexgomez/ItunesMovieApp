package com.rhexgomez.itunes.movie.app.source.cache

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

/**
 * The SQLite table that stores the movie information.
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey @ColumnInfo(name = "movie_id") var id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "currency") var currency: String,
    @ColumnInfo(name = "short_desc") var shortDesc: String?,
    @ColumnInfo(name = "long_desc") var longDesc: String?,
    @ColumnInfo(name = "release_date") var releaseDate: Date,
    @ColumnInfo(name = "genre") var genre: String,
    @ColumnInfo(name = "actor") var actor: String,
    @ColumnInfo(name = "preview") var preview: Uri?,
    @ColumnInfo(name = "image") var image: Uri?
)

/**
 * The SQLite table that stores the user's movie browsing history.
 * It is primarily about storing date viewed of the movie!
 */
@Entity(
    tableName = "watch_history",
    foreignKeys = [ForeignKey(
        entity = MovieEntity::class,
        parentColumns = ["movie_id"],
        childColumns = ["movie_id"]
    )]
)
data class WatchHistoryEntity(
    @PrimaryKey @ColumnInfo(name = "movie_id") var movieId: String,
    @ColumnInfo(name = "last_watch") var lastWatch: Date
)

/**
 * The SQLite table that stores the search results which is mapped
 * to a certain keyword.
 */
@Entity(
    tableName = "search_results",
    primaryKeys = ["movie_id", "keyword"],
    foreignKeys = [ForeignKey(
        entity = MovieEntity::class,
        parentColumns = ["movie_id"],
        childColumns = ["movie_id"]
    )]
)
data class SearchResultsEntity(
    @ColumnInfo(name = "keyword") var keyword: String,
    @ColumnInfo(name = "movie_id") var movieId: String
)