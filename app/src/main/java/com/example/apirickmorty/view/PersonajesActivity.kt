package com.example.apirickmorty.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apirickmorty.databinding.ActivityPersonajesBinding
import com.example.apirickmorty.model.APIService
import com.example.apirickmorty.model.dataclass.Personaje
import com.example.apirickmorty.model.adapter.PersonajesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PersonajesActivity : AppCompatActivity() {
    private lateinit var b: ActivityPersonajesBinding
    private lateinit var personajesAdapter : PersonajesAdapter
    private val personajesList = mutableListOf<Personaje>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityPersonajesBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getIntExtra("id", -1)
        val opcion = intent.getIntExtra("opcion", -1)
        var intent: Intent

        initRecyclerView()

        if(opcion == 1) {
            b.ivAtras.setOnClickListener {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            loadPersonajes()
        } else if(opcion == 2) {
            b.ivAtras.setOnClickListener {
                intent = Intent(this, EpisodeActivity::class.java)
                startActivity(intent)
            }
            loadPersonajes(id)
        }
    }

    private fun initRecyclerView() {
        personajesAdapter = PersonajesAdapter(personajesList)
        b.rvPersonajes.layoutManager = LinearLayoutManager(this)
        b.rvPersonajes.adapter = personajesAdapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadPersonajes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val firstPageResponse = getRetrofit()
                    .create(APIService::class.java)
                    .getCharactersByPage(1)

                val deferredPages = mutableListOf<Deferred<List<Personaje>>>()

                for (page in 1..firstPageResponse.info.pages) {
                    val deferred = async(Dispatchers.IO) {
                        try {
                            val response = getRetrofit()
                                .create(APIService::class.java)
                                .getCharactersByPage(page)
                            response.results
                        } catch (e: Exception) {
                            emptyList()
                        }
                    }
                    deferredPages.add(deferred)
                }
                withContext(Dispatchers.Main) {
                    personajesList.clear()
                    personajesList.addAll(deferredPages.awaitAll().flatten())
                    personajesAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@PersonajesActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun loadPersonajes(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val episodio = getRetrofit()
                    .create(APIService::class.java)
                    .getEpisodio(id)

                val characterIds = episodio.characters.map { url ->
                    url.split("/").last().toInt()
                }

                val deferredPersonajes = characterIds.map { characterId ->
                    async(Dispatchers.IO) {
                        getRetrofit()
                            .create(APIService::class.java)
                            .getPersonaje(characterId)
                    }
                }

                val personajes = deferredPersonajes.awaitAll()

                withContext(Dispatchers.Main) {
                    personajesList.clear()
                    personajesList.addAll(personajes)
                    personajesAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@PersonajesActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}