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

class PopularPlacesAdapter(
    private var lugares: List<Lugar>,
    private val context: Context,
    private val listener: OnItemClickListener // Interface para manejar clics
) : RecyclerView.Adapter<PopularPlacesAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(lugar: Lugar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lugar = lugares.getOrNull(position)
        holder.bindPlace(lugar)
    }

    override fun getItemCount(): Int {
        return lugares.size
    }

    fun updateData(newLugares: List<Lugar>) {
        this.lugares = newLugares
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeImage: ImageView = itemView.findViewById(R.id.place_image)
        private val placeName: TextView = itemView.findViewById(R.id.place_name)

        fun bindPlace(lugar: Lugar?) {
            if (lugar != null) {
                Glide.with(context)
                    .load(lugar.imagenUrl)
                    .into(placeImage)

                placeName.text = lugar.nombre

                // Configurar clics
                placeImage.setOnClickListener { listener.onItemClick(lugar) }
                placeName.setOnClickListener { listener.onItemClick(lugar) }

                // Asegurarse de que las vistas estén visibles
                placeImage.visibility = View.VISIBLE
                placeName.visibility = View.VISIBLE
            } else {
                // Ocultar vistas si no hay datos
                placeImage.setImageDrawable(null)
                placeName.text = ""
                placeImage.visibility = View.GONE
                placeName.visibility = View.GONE
            }
        }
    }
}