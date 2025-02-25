package com.example.apirickmorty.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.apirickmorty.R
import com.example.apirickmorty.databinding.ActivityMainBinding

/**
 * Activity principal que sirve como punto de entrada a la aplicación.
 * Proporciona navegación a las diferentes secciones de la app.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var b : ActivityMainBinding
    private var secret = 0

    /**
     * Inicializa la actividad y configura los listeners para los botones de navegación.
     *
     * @param savedInstanceState Estado guardado de la actividad si existe.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(b.root)
        ViewCompat.setOnApplyWindowInsetsListener(b.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        b.btTemporadas.setOnClickListener {
            val intent = Intent(this, EpisodeActivity::class.java)
            startActivity(intent)
        }

        b.btEpisodios.setOnClickListener {
            val intent = Intent(this, PersonajesActivity::class.java)
            intent.putExtra("opcion", 1)
            startActivity(intent)
        }

        b.imPortadaPrincipal.setOnClickListener {
            if(secret >= 7) {
                b.imPortadaPrincipal.visibility = View.INVISIBLE
                b.btEpisodios.visibility  = View.INVISIBLE
                b.btTemporadas.visibility  = View.INVISIBLE

                Glide.with(this)
                    .asGif()
                    .load(R.drawable.easteregg)
                    .into(b.imEasterEgg)
                b.imEasterEgg.visibility  = View.VISIBLE

                android.os.Handler().postDelayed({
                    b.imPortadaPrincipal.visibility = View.VISIBLE
                    b.btEpisodios.visibility = View.VISIBLE
                    b.btTemporadas.visibility = View.VISIBLE
                    b.imEasterEgg.visibility = View.GONE
                }, 9950)

                secret = 0
            } else {
                secret++
            }
        }
    }
}