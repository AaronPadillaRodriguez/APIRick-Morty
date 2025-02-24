package com.example.apirickmorty.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apirickmorty.R
import com.example.apirickmorty.databinding.ActivityEpisodeBinding
import com.example.apirickmorty.model.APIService
import com.example.apirickmorty.model.dataclass.Episodio
import com.example.apirickmorty.model.adapter.EpisodiosAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EpisodeActivity : AppCompatActivity() {
    private lateinit var b: ActivityEpisodeBinding
    private lateinit var episodioAdapter : EpisodiosAdapter
    private val episodesList = mutableListOf<Episodio>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityEpisodeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        b.rvEpisodios.setBackgroundColor(Color.TRANSPARENT)

        initRecyclerView()
        loadTemporadas()

        b.sTemporadas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val temporadaSeleccionada = parent.getItemAtPosition(position).toString()
                loadEpisodios(temporadaSeleccionada.toInt())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        b.ivAtras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("opcion", 2)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        episodioAdapter = EpisodiosAdapter(episodesList) {
            onClickListener(it)
        }
        b.rvEpisodios.layoutManager = LinearLayoutManager(this)
        b.rvEpisodios.adapter = episodioAdapter
    }

    private fun getRetrofit(): Retrofit =
            Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private fun loadTemporadas() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val llamada = getRetrofit()
                    .create(APIService::class.java)
                    .getTemporadas()
                val numTemporadas = llamada.results.size

                val cantidad = (1..numTemporadas).toList()

                val spinnerAdapter = ArrayAdapter(
                    this@EpisodeActivity,
                    R.layout.spinner_item,
                    cantidad
                )

                spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

                launch(Dispatchers.Main) {
                    b.sTemporadas.adapter = spinnerAdapter
                }
            } catch (e: Exception) {
                launch(Dispatchers.Main) {
                    Toast.makeText(
                        this@EpisodeActivity,
                        "ERROR! Fallo al cargar el spinner",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadEpisodios(temp: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val seasonPattern = "S0$temp"

                val llamada1 = getRetrofit()
                    .create(APIService::class.java)
                    .getEpisodiosByTemporada(seasonPattern)

                val deferredEpisodes = llamada1.results.map { episodio ->
                    async(Dispatchers.IO) {
                        episodio
                    }
                }

                val episodes = deferredEpisodes.awaitAll()

                withContext(Dispatchers.Main) {
                    episodesList.clear()
                    episodesList.addAll(episodes)
                    episodioAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@EpisodeActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onClickListener(id: Episodio) {
        val intent = Intent(this, PersonajesActivity::class.java)
        intent.putExtra("id", id.id)
        intent.putExtra("opcion", 2)
        startActivity(intent)
    }
}