package com.example.apirickmorty.model.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apirickmorty.R
import com.example.apirickmorty.databinding.ItemPersonajeBinding
import com.example.apirickmorty.model.dataclass.Personaje
import com.squareup.picasso.Picasso

/**
 * Adaptador para RecyclerView que muestra una lista de personajes de Rick y Morty.
 *
 * @property datos Lista de objetos Personaje que se mostrarán en el RecyclerView.
 */
class PersonajesAdapter(private val datos: List<Personaje>) : RecyclerView.Adapter<PersonajesAdapter.PersonajeViewHolder>() {

    /**
     * Crea un nuevo ViewHolder inflando el layout del item de personaje.
     *
     * @param parent ViewGroup padre donde se añadirá el ViewHolder.
     * @param viewType Tipo de vista a crear.
     * @return Una instancia de PersonajeViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonajeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PersonajeViewHolder(layoutInflater.inflate(R.layout.item_personaje, parent, false))
    }

    /**
     * Devuelve el numero total de elementos en el adaptador.
     *
     * @return El tamaño de la lista de datos.
     */
    override fun getItemCount(): Int = datos.size

    /**
     * Vincula los datos de un personaje a un ViewHolder en la posición especificada.
     *
     * @param holder ViewHolder que será actualizado.
     * @param position Posición del elemento en la lista de datos.
     */
    override fun onBindViewHolder(holder: PersonajeViewHolder, position: Int) {
        holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        val item = datos[position]
        holder.bind(item)
    }

    /**
     * ViewHolder personalizado para mostrar información de personajes.
     *
     * @property view Vista inflada del layout del item de personaje.
     */
    inner class PersonajeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val b = ItemPersonajeBinding.bind(view)

        /**
         * Vincula un objeto Personaje a este ViewHolder.
         *
         * @param personaje Objeto Personaje con los datos a mostrar.
         */
        fun bind(personaje: Personaje) {
            b.tvNombre.text = personaje.name
            b.tvStatus.text = personaje.status
            b.tvSpecies.text = "Especie: " + personaje.species
            b.tvGender.text = "Genero: " + personaje.gender
            b.tvOrigen.text = "Origen: " + personaje.origen.name
            b.tvLocation.text = "Localizacion: " + personaje.location.name
            if(personaje.type.isBlank()) b.tvType.text = "Tipo: Desconocido" else b.tvType.text = "Tipo: " + personaje.type

            Picasso.get()
                .load(personaje.image)
                .resize(200, 200)
                .placeholder(R.drawable.cargar)
                .error(R.drawable.error)
                .into(b.ivPersonaje)
        }
    }
}