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
        // Inflar el diseño del ítem que contiene dos lugares (place_image, place_name, place_image2, place_name2)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Obtener los dos lugares correspondientes a esta posición
        val lugar1 = lugares?.getOrNull(position * 2) // Primer lugar
        val lugar2 = lugares?.getOrNull(position * 2 + 1) // Segundo lugar

        // Vincular los datos a las vistas
        holder.bindPlace1(lugar1)
        holder.bindPlace2(lugar2)
    }

    override fun getItemCount(): Int {
        // Dividir la lista en grupos de 2 elementos
        return if (lugares == null) 0 else (lugares!!.size + 1) / 2
    }

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
                // Cargar la imagen usando Glide
                Glide.with(context)
                    .load(lugar.imagenUrl)
                    .into(placeImage1)

                // Configurar el nombre del lugar
                placeName1.text = lugar.nombre

                // Configurar el listener de clics para abrir el detalle
                placeImage1.setOnClickListener {
                    openDetailActivity(lugar)
                }
                placeName1.setOnClickListener {
                    openDetailActivity(lugar)
                }

                // Asegurarse de que las vistas sean visibles
                placeImage1.visibility = View.VISIBLE
                placeName1.visibility = View.VISIBLE
            } else {
                // Limpiar las vistas si no hay datos
                placeImage1.setImageDrawable(null)
                placeName1.text = ""
                placeImage1.visibility = View.GONE
                placeName1.visibility = View.GONE
            }
        }

        fun bindPlace2(lugar: Lugar?) {
            if (lugar != null) {
                // Cargar la imagen usando Glide
                Glide.with(context)
                    .load(lugar.imagenUrl)
                    .into(placeImage2)

                // Configurar el nombre del lugar
                placeName2.text = lugar.nombre

                // Configurar el listener de clics para abrir el detalle
                placeImage2.setOnClickListener {
                    openDetailActivity(lugar)
                }
                placeName2.setOnClickListener {
                    openDetailActivity(lugar)
                }

                // Asegurarse de que las vistas sean visibles
                placeImage2.visibility = View.VISIBLE
                placeName2.visibility = View.VISIBLE
            } else {
                // Limpiar las vistas si no hay datos
                placeImage2.setImageDrawable(null)
                placeName2.text = ""
                placeImage2.visibility = View.GONE
                placeName2.visibility = View.GONE
            }
        }

        private fun openDetailActivity(lugar: Lugar) {
            // Crear un Intent para abrir la actividad de detalles
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("id", lugar.id)
                putExtra("nombre", lugar.nombre)
                putExtra("descripcion", lugar.descripcion)
                putExtra("ubicacion", lugar.ubicacion)
                putExtra("valoracion", lugar.valoracion)
                putExtra("imagenUrl", lugar.imagenUrl)
                putExtra("tipoEstablecimiento", lugar.tipoEstablecimiento)
            }
            context.startActivity(intent)
        }
    }
}