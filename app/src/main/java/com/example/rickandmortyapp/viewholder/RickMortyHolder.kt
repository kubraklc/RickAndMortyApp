package com.example.rickandmortyapp.viewholder

import android.content.Context
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapp.HomeActivity
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.model.Result

class RickMortyHolder(
    itemView : View,
    private val context : HomeActivity,
     val clickListener : (Result) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    val  status: TextView = itemView.findViewById(R.id.rmCharacterStatus)
    val  gender: TextView = itemView.findViewById(R.id.rmCharacterGender)
    val  name  : TextView = itemView.findViewById(R.id.rmCharacterName)
    val  img   : ImageView = itemView.findViewById(R.id.rmCharacterProfile)
    val  statusImg: ImageView = itemView.findViewById(R.id.imgCharacterStatus)

    init {
        itemView.setOnClickListener {
            clickListener
        }
    }
}