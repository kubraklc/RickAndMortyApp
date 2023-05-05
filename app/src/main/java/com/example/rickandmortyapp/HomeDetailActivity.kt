package com.example.rickandmortyapp

import android.graphics.*
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rickandmortyapp.model.RickMortyDetailResponseX
import com.example.rickandmortyapp.retrofit.RickMortyService
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class HomeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)




        var intent = intent
        var characterId = intent.extras?.getString("id")

     RickMortyService.rickMortyApi.getCharacterDetail(characterId).enqueue(object : Callback<RickMortyDetailResponseX>{
         override fun onResponse(
             call: Call<RickMortyDetailResponseX>,
             response: Response<RickMortyDetailResponseX>,
         ) {
             val image = findViewById<ImageView>(R.id.rmCharacterProfile)
             val name = findViewById<TextView>(R.id.rmCharacterName)
             val species =findViewById<TextView>(R.id.rmCharacterSpecies)
             val status = findViewById<TextView>(R.id.rmCharacterStatus)
             val gender = findViewById<TextView>(R.id.rmCharacterGender)
             val origin = findViewById<TextView>(R.id.rmCharacterOriginLocation)
             val location = findViewById<TextView>(R.id.rmCharacterLastLocation)
             val episode= findViewById<TextView>(R.id.rmCharacterEpisode)

             name.text = response.body()!!.name
             species.text = response.body()!!.species
             status.text = response.body()!!.status
             gender.text = response.body()!!.gender
             origin.text = response.body()!!.origin.name
             location.text = response.body()!!.location.name
             episode.text = response.body()!!.episode.firstOrNull()

             Glide.with(this@HomeDetailActivity)
                 .load(response.body()!!.image)

                 .into(image)

             image.scaleType = ImageView.ScaleType.CENTER
         }

         override fun onFailure(call: Call<RickMortyDetailResponseX>, t: Throwable) {
             Log.d("onFailure->", t.message.toString())
         }

     })
    }
}