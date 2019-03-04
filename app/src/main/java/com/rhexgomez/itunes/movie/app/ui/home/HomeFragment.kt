package com.rhexgomez.itunes.movie.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhexgomez.itunes.movie.app.R
import com.rhexgomez.itunes.movie.app.databinding.FragmentHomeBinding
import com.rhexgomez.itunes.movie.app.ui.MainActivity

/**
 * A Fragment that shows recently viewed movies and searched movies.
 */
class HomeFragment : Fragment() {


    private lateinit var binder: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binder = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        return binder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity as MainActivity
        val viewModel = activity.homeViewModel

        val watchedMoviesAdapter = WatchedMovieAdapter(viewModel)
        val searchedMoviesAdapter = SearchedMoviesAdapter(viewModel)
        binder.watchedMovies.apply {
            adapter = watchedMoviesAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binder.searchedMovies.apply {
            adapter = searchedMoviesAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.search("star")
        viewModel.watchedMovies.observe(this, Observer {
            if (it.isEmpty()) {
                binder.recentLabel.visibility = View.GONE
                binder.watchedMovies.visibility = View.GONE
            } else {
                binder.recentLabel.visibility = View.VISIBLE
                binder.watchedMovies.visibility = View.VISIBLE
            }

            watchedMoviesAdapter.movies = it
        })
        viewModel.searchedMovies.observe(this, Observer {
            viewModel.setCount(it.size)
            searchedMoviesAdapter.submitList(it)
        })

        binder.searchBox.setOnEditorActionListener { _, actionId, _ ->
            val keyword = binder.searchBox.text.toString()
            if (actionId == EditorInfo.IME_ACTION_DONE && keyword.isNotEmpty()) {
                viewModel.search(keyword)
                true
            }
            false
        }
    }
}