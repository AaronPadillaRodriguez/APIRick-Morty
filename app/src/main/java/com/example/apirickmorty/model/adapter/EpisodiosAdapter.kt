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

class EpisodiosAdapter(private val datos: List<Episodio>, private val fn: (Episodio) -> Unit) : RecyclerView.Adapter<EpisodiosAdapter.EpisodiosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodiosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EpisodiosViewHolder(layoutInflater.inflate(R.layout.item_episode, parent, false))
    }

    override fun getItemCount(): Int = datos.size

    override fun onBindViewHolder(holder: EpisodiosViewHolder, position: Int) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            val item = datos[position]
            holder.bind(item)
    }

    inner class EpisodiosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val b = ItemEpisodeBinding.bind(view)

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

        private fun extractSeasonAndEpisode(episodeName: String): Pair<Int, Int> {
            val parts = episodeName.split("E")
            val season = parts[0].replace("S", "").toInt()
            val episode = parts[1].toInt()
            return Pair(season, episode)
        }

        private fun getEpisodeImageUrl(season: Int, episode: Int): String? {
            return EnumImagenes.entries.find { it.season == season && it.episode == episode }?.imageUrl
        }
    }
}