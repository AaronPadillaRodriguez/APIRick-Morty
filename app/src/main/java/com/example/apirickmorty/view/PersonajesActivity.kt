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

/**
 * Activity que muestra la lista de personajes de Rick y Morty.
 * Puede mostrar todos los personajes o solo los de un episodio específico.
 */
class PersonajesActivity : AppCompatActivity() {
    private lateinit var b: ActivityPersonajesBinding
    private lateinit var personajesAdapter : PersonajesAdapter
    private val personajesList = mutableListOf<Personaje>()

    /**
     * Inicializa la actividad, configura los listeners y carga los datos según el modo seleccionado.
     *
     * @param savedInstanceState Estado guardado de la actividad si existe.
     */
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

        initRecyclerView()

        if(opcion == 1) {
            loadPersonajes()
        } else if(opcion == 2) {
            loadPersonajes(id)
        }

        b.ivAtras.setOnClickListener {
            finish()
        }
    }

    /**
     * Inicializa el RecyclerView con su adaptador.
     */
    private fun initRecyclerView() {
        personajesAdapter = PersonajesAdapter(personajesList)
        b.rvPersonajes.layoutManager = LinearLayoutManager(this)
        b.rvPersonajes.adapter = personajesAdapter
    }

    /**
     * Crea y configura una instancia de Retrofit para comunicarse con la API.
     *
     * @return Instancia configurada de Retrofit.
     */
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Carga todos los personajes disponibles en la API.
     * Realiza múltiples peticiones de paginación para obtener la lista completa.
     */
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

    /**
     * Carga los personajes que aparecen en un episodio específico.
     *
     * @param id ID del episodio cuyos personajes se quieren cargar.
     */
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