package com.example.odisea.adapters

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
import com.example.odisea.R
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
        val lugar1 = lugares?.getOrNull(position * 2) // Primer lugar
        val lugar2 = lugares?.getOrNull(position * 2 + 1) // Segundo lugar

        holder.bindPlace1(lugar1)
        holder.bindPlace2(lugar2)
    }

    override fun getItemCount(): Int = if (lugares == null) 0 else (lugares!!.size + 1) / 2

    // Método para actualizar los datos del adaptador
    fun updateData(newLugares: List<Lugar>) {
        this.lugares = newLugares
        notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeImage1: ImageView = itemView.findViewById(R.id.place_image)
        private val placeName1: TextView = itemView.findViewById(R.id.place_name)
        private val placeImage2: ImageView = itemView.findViewById(R.id.place_image2)
        private val placeName2: TextView = itemView.findViewById(R.id.place_name2)

        fun bindPlace1(lugar: Lugar?) {
            if (lugar != null) {
                Glide.with(context).load(lugar.imagenUrl).into(placeImage1)
                placeName1.text = lugar.nombre

                // Configurar el listener de clics para el primer lugar
                itemView.setOnClickListener {
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
            } else {
                // Limpiar las vistas si no hay datos
                placeImage1.setImageDrawable(null)
                placeName1.text = ""
            }
        }

        fun bindPlace2(lugar: Lugar?) {
            if (lugar != null) {
                Glide.with(context).load(lugar.imagenUrl).into(placeImage2)
                placeName2.text = lugar.nombre

                // Configurar el listener de clics para el segundo lugar
                itemView.setOnClickListener {
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
            } else {
                // Limpiar las vistas si no hay datos
                placeImage2.setImageDrawable(null)
                placeName2.text = ""
            }
        }
    }
}