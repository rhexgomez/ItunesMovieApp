package com.rhexgomez.itunes.movie.app.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rhexgomez.itunes.movie.app.databinding.FragmentMovieDetailsBinding
import com.rhexgomez.itunes.movie.app.ui.MainActivity
import com.rhexgomez.itunes.movie.app.util.toCurrency
import com.squareup.picasso.Picasso
import android.content.Intent
import com.squareup.picasso.Transformation
import android.graphics.*
import com.rhexgomez.itunes.movie.app.R


class MovieDetailsFragment : Fragment() {

    private lateinit var binder: FragmentMovieDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        return binder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = activity as MainActivity
        val viewModel = activity.homeViewModel
        viewModel.viewedMovie.observe(this, Observer { entity ->
            if (entity != null) {
                binder.apply {
                    movieTitle.text = entity.name
                    movieDesc.text = entity.shortDesc
                    moviePrice.text = entity.price.toCurrency(entity.currency)
                    movieActor.text = entity.actor
                    movieGenre.text = entity.genre
                    Picasso.get()
                        .load(entity.image)
                        .centerCrop()
                        .fit()
                        .into(movieImage)

                    playVideo.setOnClickListener {
                        if (entity.preview != null) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(entity.preview, "video/*")
                            startActivity(intent)
                        }
                    }

                    movieTitle.paint
                }
            }
        })
    }

}