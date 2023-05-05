package com.example.rickandmortyapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Movie
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.HomeActivity
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.RecRowBinding
import com.example.rickandmortyapp.model.Result
import java.util.*
import kotlin.collections.ArrayList



class FilterAdapter(
    private val movieList: ArrayList<Result>
):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var movieFilterList = ArrayList<Result>()


    class MovieHolder(var viewBinding: RecRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
    }

    init {
        movieFilterList = movieList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            RecRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val sch = MovieHolder(binding)
        return sch
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rickHolder = holder as MovieHolder

        rickHolder.viewBinding.rmCharacterName.text = movieFilterList[position].name
        rickHolder.viewBinding.rmCharacterStatus.text = movieFilterList[position].status
        rickHolder.viewBinding.rmCharacterGender.text = movieFilterList[position].gender

        if(movieFilterList[position].status == "Dead") {
            holder.viewBinding.imgCharacterStatus.setColorFilter(Color.RED)
        } else if (movieFilterList[position].status == "Alive") {
            holder.viewBinding.imgCharacterStatus.setColorFilter(Color.GREEN)
        } else {
            holder.viewBinding.imgCharacterStatus.setColorFilter(Color.GRAY)
        }

        Glide.with(holder.itemView)
            .load(movieFilterList[position].image)
            .into(rickHolder.viewBinding.rmCharacterProfile)

    }

    override fun getItemCount(): Int {
        return movieFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint : CharSequence?): FilterResults {
                //Aranan yazımız
                 val movieSearch = constraint.toString()
                //Arama alanında bir yazı aranmadıysa tüm movieleri moviefilterliste akataralım
                if (movieSearch.isEmpty()){
                    movieFilterList = movieList
                } else {
                    val resultList = ArrayList<Result>()
                        //Aranan movie add metodu ile listeye eklenir
                        for (movie in movieList) {
                            if (movie.name.lowercase().contains(movieSearch.lowercase())) {
                                resultList.add(movie)
                            }
                        }
                        movieFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = movieFilterList
                filterResults.count = movieFilterList.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                movieFilterList = results?.values as ArrayList<Result> /* = java.util.ArrayList<kotlin.String> */
                notifyDataSetChanged()
            }
            fun filterList(filteredList:  List<Result>) {
                var charList =
                    filteredList as ArrayList<Result> /* = java.util.ArrayList<com.example.rickandmortyapp.model.Result> *//* = java.util.ArrayList<com.example.rickandmortyapp.model.Result> */
                notifyDataSetChanged()

            }
        }

    }

}


