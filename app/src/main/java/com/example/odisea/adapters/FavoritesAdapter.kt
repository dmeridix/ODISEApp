package com.example.odisea.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.odisea.R
import com.example.odisea.data.Lugar

class FavoritesAdapter(
    private var lugares: List<Lugar>,
    private val context: Context
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflar el diseño del item para el RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Obtener el lugar correspondiente a la posición actual
        val lugar = lugares.getOrNull(position)

        if (lugar != null) {
            holder.bindPlace(lugar)
        } else {
            holder.hidePlace()
        }
    }

    override fun getItemCount(): Int {
        // Devolver el tamaño total de la lista de lugares
        return lugares.size
    }

    fun updateData(newLugares: List<Lugar>) {
        // Actualizar la lista de lugares y notificar cambios al adaptador
        this.lugares = newLugares
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Referencias a las vistas del lugar
        private val placeImage: ImageView = itemView.findViewById(R.id.place_image)
        private val placeName: TextView = itemView.findViewById(R.id.nameTextView)
        private val placeLocation: TextView = itemView.findViewById(R.id.locationTextView)
        private val placeRating: TextView = itemView.findViewById(R.id.rating)

        /**
         * Vincula los datos del lugar.
         */
        fun bindPlace(lugar: Lugar) {
            Glide.with(context)
                .load(lugar.imagenUrl)
                .into(placeImage)

            placeName.text = lugar.nombre
            placeLocation.text = lugar.ubicacion ?: "Ubicación no disponible"
            placeRating.text = lugar.valoracion?.toString() ?: "Sin calificación"

            placeImage.visibility = View.VISIBLE
            placeName.visibility = View.VISIBLE
            placeLocation.visibility = View.VISIBLE
            placeRating.visibility = View.VISIBLE
        }

        /**
         * Oculta el lugar si no hay datos disponibles.
         */
        fun hidePlace() {
            placeImage.visibility = View.GONE
            placeName.visibility = View.GONE
            placeLocation.visibility = View.GONE
            placeRating.visibility = View.GONE
        }
    }
}