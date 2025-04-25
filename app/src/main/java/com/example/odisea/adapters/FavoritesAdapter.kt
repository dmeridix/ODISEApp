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
        // Agrupar los lugares en pares
        val lugar1 = lugares.getOrNull(position * 2)
        val lugar2 = lugares.getOrNull(position * 2 + 1)

        // Vincular el primer lugar (si existe)
        if (lugar1 != null) {
            holder.bindFirstPlace(lugar1)
        } else {
            holder.hideFirstPlace()
        }

        // Vincular el segundo lugar (si existe)
        if (lugar2 != null) {
            holder.bindSecondPlace(lugar2)
        } else {
            holder.hideSecondPlace()
        }
    }

    override fun getItemCount(): Int {
        // Dividir la lista en grupos de 2 elementos
        return (lugares.size + 1) / 2
    }

    fun updateData(newLugares: List<Lugar>) {
        // Actualizar la lista de lugares y notificar cambios al adaptador
        this.lugares = newLugares
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Referencias a las vistas del primer lugar
        private val placeImage1: ImageView = itemView.findViewById(R.id.place_image)
        private val placeName1: TextView = itemView.findViewById(R.id.place_name)

        // Referencias a las vistas del segundo lugar
        private val placeImage2: ImageView = itemView.findViewById(R.id.place_image2)
        private val placeName2: TextView = itemView.findViewById(R.id.place_name2)

        /**
         * Vincula los datos del primer lugar.
         */
        fun bindFirstPlace(lugar: Lugar) {
            Glide.with(context).load(lugar.imagenUrl).into(placeImage1)
            placeName1.text = lugar.nombre
            placeImage1.visibility = View.VISIBLE
            placeName1.visibility = View.VISIBLE
        }

        /**
         * Vincula los datos del segundo lugar.
         */
        fun bindSecondPlace(lugar: Lugar) {
            Glide.with(context).load(lugar.imagenUrl).into(placeImage2)
            placeName2.text = lugar.nombre
            placeImage2.visibility = View.VISIBLE
            placeName2.visibility = View.VISIBLE
        }

        /**
         * Oculta el primer lugar si no hay datos disponibles.
         */
        fun hideFirstPlace() {
            placeImage1.visibility = View.GONE
            placeName1.visibility = View.GONE
        }

        /**
         * Oculta el segundo lugar si no hay datos disponibles.
         */
        fun hideSecondPlace() {
            placeImage2.visibility = View.GONE
            placeName2.visibility = View.GONE
        }
    }
}