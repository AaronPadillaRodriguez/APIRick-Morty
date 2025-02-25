package com.example.apirickmorty.model.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apirickmorty.R
import com.example.apirickmorty.databinding.ItemEpisodeBinding
import com.example.apirickmorty.model.OvalTransformation
import com.example.apirickmorty.model.dataclass.Episodio
import com.example.apirickmorty.model.enums.EnumImagenes
import com.squareup.picasso.Picasso

/**
 * Adaptador para RecyclerView que muestra una lista de episodios de Rick y Morty.
 *
 * @property datos Lista de objetos Episodio que se mostrarán en el RecyclerView.
 * @property fn Función lambda que se ejecuta cuando un episodio es seleccionado.
 */
class EpisodiosAdapter(private val datos: List<Episodio>, private val fn: (Episodio) -> Unit) : RecyclerView.Adapter<EpisodiosAdapter.EpisodiosViewHolder>() {

    /**
     * Crea un nuevo ViewHolder inflando el layout del item de episodio.
     *
     * @param parent ViewGroup padre donde se añadira el ViewHolder.
     * @param viewType Tipo de vista a crear.
     * @return Una instancia de EpisodiosViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodiosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EpisodiosViewHolder(layoutInflater.inflate(R.layout.item_episode, parent, false))
    }

    /**
     * Devuelve el numero total de elementos en el adaptador.
     *
     * @return El tamaño de la lista de datos.
     */
    override fun getItemCount(): Int = datos.size

    /**
     * Vincula los datos de un episodio a un ViewHolder en la posición especificada.
     *
     * @param holder ViewHolder que será actualizado.
     * @param position Posición del elemento en la lista de datos.
     */
    override fun onBindViewHolder(holder: EpisodiosViewHolder, position: Int) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            val item = datos[position]
            holder.bind(item)
    }

    /**
     * ViewHolder personalizado para mostrar información de episodios.
     *
     * @property view Vista inflada del layout del item de episodio.
     */
    inner class EpisodiosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val b = ItemEpisodeBinding.bind(view)

        /**
         * Vincula un objeto Episodio a este ViewHolder.
         *
         * @param episodio Objeto Episodio con los datos a mostrar.
         */
        fun bind(episodio: Episodio) {
                b.tvNombre.text = episodio.name
                val (temp, capt) = extractSeasonAndEpisode(episodio.episode)
                val imageUrl = getEpisodeImageUrl(temp, capt)

                Picasso.get()
                    .load(imageUrl)
                    .transform(OvalTransformation())
                    .into(b.ivFotoEpisodio)

                itemView.setOnClickListener {
                    fn(episodio)
                }
        }

        /**
         * Extrae el número de temporada y episodio a partir de un string con formato "SxxExx".
         *
         * @param episodeName String con formato "SxxExx" (por ejemplo, "S01E03").
         * @return Par que contiene (temporada, episodio).
         */
        private fun extractSeasonAndEpisode(episodeName: String): Pair<Int, Int> {
            val parts = episodeName.split("E")
            val season = parts[0].replace("S", "").toInt()
            val episode = parts[1].toInt()
            return Pair(season, episode)
        }

        /**
         * Obtiene la URL de la imagen para un episodio específico.
         *
         * @param season Numero de temporada.
         * @param episode Numero de episodio.
         * @return URL de la imagen para el episodio o null si no se encuentra.
         */
        private fun getEpisodeImageUrl(season: Int, episode: Int): String? {
            return EnumImagenes.entries.find { it.season == season && it.episode == episode }?.imageUrl
        }
    }
}