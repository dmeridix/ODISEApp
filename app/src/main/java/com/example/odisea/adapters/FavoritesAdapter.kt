package com.example.odisea.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.odisea.R
import com.example.odisea.data.Lugar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoritesAdapter(
    private var lugares: List<Lugar>,
    private val favoritosIds: Set<Int>, // IDs de lugares favoritos
    private val context: Context,
    private val onItemClick: (Lugar) -> Unit,
    private val onFavoriteClick: (Lugar) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lugar = lugares.getOrNull(position)
        if (lugar != null) {
            holder.bindPlace(lugar)
        } else {
            holder.hidePlace()
        }
    }

    override fun getItemCount(): Int = lugares.size

    fun updateData(newLugares: List<Lugar>, nuevosFavoritos: Set<Int>) {
        this.lugares = newLugares
        (favoritosIds as MutableSet).clear()
        favoritosIds.addAll(nuevosFavoritos)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeImage: ImageView = itemView.findViewById(R.id.topBackground)
        private val placeName: TextView = itemView.findViewById(R.id.nameTextView)
        private val placeLocation: TextView = itemView.findViewById(R.id.locationTextView)
        private val placeRating: TextView = itemView.findViewById(R.id.rating)
        private val bookmarkFab: FloatingActionButton =
            itemView.findViewById(R.id.fab_add_to_favorites)

        fun bindPlace(lugar: Lugar) {
            Glide.with(context)
                .load(lugar.imagenUrl)
                .into(placeImage)

            placeName.text = lugar.nombre
            placeLocation.text = lugar.ubicacion ?: "Ubicación no disponible"
            placeRating.text = lugar.valoracion?.toString() ?: "Sin calificación"

            val esFavorito = favoritosIds.contains(lugar.id)
            bookmarkFab.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    if (esFavorito) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
                )
            )

            bookmarkFab.setOnClickListener { onFavoriteClick(lugar) }
            itemView.setOnClickListener { onItemClick(lugar) }

            placeImage.visibility = View.VISIBLE
            placeName.visibility = View.VISIBLE
            placeLocation.visibility = View.VISIBLE
            placeRating.visibility = View.VISIBLE
        }

        fun hidePlace() {
            placeImage.visibility = View.GONE
            placeName.visibility = View.GONE
            placeLocation.visibility = View.GONE
            placeRating.visibility = View.GONE
        }
    }
}
