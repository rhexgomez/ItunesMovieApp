package com.rhexgomez.itunes.movie.app.source.cache.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rhexgomez.itunes.movie.app.source.cache.MovieEntity
import com.rhexgomez.itunes.movie.app.source.cache.WatchHistoryEntity

@Dao
abstract class WatchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movie: WatchHistoryEntity)

    @Query("SELECT movies.* FROM movies INNER JOIN watch_history ON movies.movie_id = watch_history.movie_id ORDER BY watch_history.last_watch DESC")
    abstract fun getMovieHistory(): LiveData<List<MovieEntity>>
}