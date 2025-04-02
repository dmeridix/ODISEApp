package com.example.odisea

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.odisea.DetailActivity
import com.example.odisea.data.Lugar

class PopularPlacesAdapter(
    private var lugares: List<Lugar>?,
    private val context: Context
) : RecyclerView.Adapter<PopularPlacesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lugar = lugares?.get(position)
        lugar?.let {
            Glide.with(context).load(it.imagenUrl).into(holder.placeImage)
            holder.placeName.text = it.nombre

            // Configurar el listener de clics
            holder.itemView.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("id", lugar.id)
                    putExtra("nombre", lugar.nombre)
                    putExtra("descripcion", lugar.descripcion)
                    putExtra("ubicacion", lugar.ubicacion)
                    putExtra("calificacion", lugar.calificacion)
                    putExtra("imagenUrl", lugar.imagenUrl)
                    putExtra("tipoEstablecimiento", lugar.tipoEstablecimiento)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = lugares?.size ?: 0

    fun updateData(newLugares: List<Lugar>) {
        this.lugares = newLugares
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.place_image)
        val placeName: TextView = itemView.findViewById(R.id.place_name)
    }
}