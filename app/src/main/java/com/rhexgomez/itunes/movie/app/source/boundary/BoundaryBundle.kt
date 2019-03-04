package com.rhexgomez.itunes.movie.app.source.boundary

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * This packages the Boundary and the trigger for count.
 */
data class BoundaryBundle<T>(
    val boundary: LiveData<PagedList<T>>,
    val itemCount: (Int) -> Unit
)