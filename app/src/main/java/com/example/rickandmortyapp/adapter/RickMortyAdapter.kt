package com.example.rickandmortyapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.HomeActivity
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.model.Result
import com.example.rickandmortyapp.model.RickMortyResponseX
import com.example.rickandmortyapp.viewholder.RickMortyHolder
import java.util.*
import kotlin.collections.ArrayList

class RickMortyAdapter(
    private var charList: ArrayList<Result>,
    private val context: HomeActivity,
    val clickListener: (Result) -> Unit,
) : RecyclerView.Adapter<RickMortyHolder>(), Filterable {


    var rickFilterList = ArrayList<Result>()

    init {
        rickFilterList = charList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RickMortyHolder {
        return RickMortyHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rm_item_home,
                parent,
                false
            ),
            context,
            clickListener
        )
    }

    override fun onBindViewHolder(holder: RickMortyHolder, position: Int) {
        val char = charList[position]

        holder.status.text = char.status
        holder.gender.text = char.gender
        holder.name.text = char.name
        holder.itemView.setOnClickListener {
            clickListener.invoke(char)
        }

        if (char.status == "Dead") {
            holder.statusImg.setColorFilter(Color.RED)
        } else if (char.status == "Alive") {
            holder.statusImg.setColorFilter(Color.GREEN)
        } else {
            holder.statusImg.setColorFilter(Color.GRAY)
        }

        Glide.with(context)
            .load(char.image)
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return charList.count()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //Aranan yazımız
                val filteredList: MutableList<Result> = mutableListOf()
                //Arama alanında bir yazı aranmadıysa tüm movieleri moviefilterliste akataralım
                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(charList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    for (movie in charList) {
                        if (movie.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                            filteredList.add(movie)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = rickFilterList
                filterResults.count = rickFilterList.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                rickFilterList =
                    results?.values as ArrayList<Result>  /* = java.util.ArrayList<kotlin.String> */
                notifyDataSetChanged()
            }
        }
    }

    fun setFilter(filteredList: List<Result>) {
        filteredList?.let {
            rickFilterList = it as ArrayList<Result>
            notifyDataSetChanged()
        }
    }


}


