package com.example.rickandmortyapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapp.adapter.RickMortyAdapter
import com.example.rickandmortyapp.databinding.ActivityHomeBinding
import com.example.rickandmortyapp.model.Result
import com.example.rickandmortyapp.model.RickMortyResponseX
import com.example.rickandmortyapp.retrofit.RickMortyService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var service: RickMortyService
    private lateinit var charList: RickMortyResponseX
    private lateinit var adapter: RickMortyAdapter
    private lateinit var recyclerView: RecyclerView
    private var rickFilterList: List<Result> = ArrayList()
    lateinit var name: List<String>
    lateinit var status: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Recyclerview
        binding.rmRecyclerview.visibility = View.VISIBLE
        val recyclerView = binding.rmRecyclerview
        binding.rmRecyclerview.layoutManager = GridLayoutManager(this@HomeActivity, 2)
        binding.rmRecyclerview.setHasFixedSize(true)

        //Listelemedeki çizgileri oluşturmak için Recyclerview özellik ekliyorum.
        val decorator = DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(decorator)

        //SearchView
        val search = findViewById<SearchView>(R.id.search)
        search.setVisibility(SearchView.VISIBLE);

        //SearchView de bulunan Arama iconu tanımlayıp rengini belirledim
        val searchIcon =
            binding.search.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        //SearchView de bulunan kapama iconunu tanımlayıp rengini belirledim
        val cancelIcon =
            binding.search.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)

        //BottomSheet OnClickListener Olayı
        binding.btnFloat.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val chipGroup = bottomSheetView.findViewById<ChipGroup>(R.id.chipGroup)
            val chipGroupGender = bottomSheetView.findViewById<ChipGroup>(R.id.chipGroupGender)
            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

            // BottomSheet içinde  btn
            val button = bottomSheetView.findViewById<Button>(R.id.bttonApply)
            button.setOnClickListener {
                onButtonClicked(chipGroup, chipGroupGender)
                bottomSheetDialog.dismiss()
            }
        }

        service = RickMortyService
        RickMortyService.rickMortyApi.getRickMortyData()
            .enqueue(object : Callback<RickMortyResponseX> {
                override fun onResponse(
                    call: Call<RickMortyResponseX>,
                    response: Response<RickMortyResponseX>,
                ) {
                    if (response.isSuccessful) {
                        charList = response.body()!!
                        rickFilterList = charList.results
                        recyclerView.adapter =
                            RickMortyAdapter(rickFilterList as ArrayList<Result>, this@HomeActivity,
                                clickListener = { result: Result -> detailItemOnClick(result) })
                    }
                }

                override fun onFailure(call: Call<RickMortyResponseX>, t: Throwable) {
                    Log.e("HOMEACTIVITYERROR", t.message.toString())
                }
            })

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    rickFilterList = charList.results
                } else {
                    rickFilterList = charList.results.filter { result ->
                        result.name.lowercase().contains(newText.lowercase())
                    }
                }
                val newAdapter =
                    RickMortyAdapter(rickFilterList as ArrayList<Result>, this@HomeActivity,
                        clickListener = { result: Result -> detailItemOnClick(result) })
                recyclerView.adapter = newAdapter
                return true
            }
        })
    }

    private fun onButtonClicked(chipGroup: ChipGroup, chipGroupGender: ChipGroup): Boolean {
        val checkedChips = mutableListOf<Chip>()
        val checkedGenderChips = mutableListOf<Chip>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            if (chip?.isChecked == true) {
                checkedChips.add(chip)
            }
        }
        for (i in 0 until chipGroupGender.childCount) {
            val chip = chipGroupGender.getChildAt(i) as? Chip
            if (chip?.isChecked == true) {
                checkedGenderChips.add(chip)
            }
        }
        var rickFilterList = charList.results
        if (checkedChips.isNotEmpty()) {
            for (chip in checkedChips) {
                when (chip.text) {
                    "Alive" -> {
                        rickFilterList = rickFilterList.filter { it.status == "Alive" }
                    }
                    "Dead" -> {
                        rickFilterList = rickFilterList.filter { it.status == "Dead" }
                    }
                    "Unknown" -> {
                        rickFilterList =
                            rickFilterList.filter { it.status == "Unknown" }
                    }
                }
            }
        } else {
            rickFilterList = charList.results
        }

        if (checkedGenderChips.isNotEmpty()) {
            for (chip in checkedGenderChips) {
                when (chip.text) {
                    "Female" -> {
                        rickFilterList = rickFilterList.filter { it.gender == "Female" }
                    }
                    "Male" -> {
                        rickFilterList = rickFilterList.filter { it.gender == "Male" }
                    }
                    "Genderless" -> {
                        rickFilterList =
                            rickFilterList.filter { it.gender == "Genderless" }
                    }
                    "Unknown Gender" -> {
                        rickFilterList =
                            rickFilterList.filter { it.gender == "Unknown" }
                    }
                }
            }
        }
        val oneAdapter = RickMortyAdapter(rickFilterList as ArrayList<Result>, this@HomeActivity,
            clickListener = { result: Result -> detailItemOnClick(result) })
        binding.rmRecyclerview.adapter = oneAdapter
        oneAdapter.notifyDataSetChanged()
        return true
    }

    private fun detailItemOnClick(result: Result) {
        val intent = Intent(this@HomeActivity, HomeDetailActivity::class.java)
        intent.putExtra("id", result.id.toString())
        startActivity(intent)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.search) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}












