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
    private var lugares: List<Lugar>?,
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
        val lugar1 = lugares?.getOrNull(position * 2)
        val lugar2 = lugares?.getOrNull(position * 2 + 1)
        holder.bindPlace1(lugar1)
        holder.bindPlace2(lugar2)
    }

    override fun getItemCount(): Int {
        return if (lugares == null) 0 else (lugares!!.size + 1) / 2
    }

    fun updateData(newLugares: List<Lugar>) {
        this.lugares = newLugares
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeImage1: ImageView = itemView.findViewById(R.id.place_image)
        private val placeName1: TextView = itemView.findViewById(R.id.place_name)
        private val placeImage2: ImageView = itemView.findViewById(R.id.place_image2)
        private val placeName2: TextView = itemView.findViewById(R.id.place_name2)

        fun bindPlace1(lugar: Lugar?) {
            if (lugar != null) {
                Glide.with(context)
                    .load(lugar.imagenUrl)
                    .into(placeImage1)

                placeName1.text = lugar.nombre

                placeImage1.setOnClickListener { listener.onItemClick(lugar) }
                placeName1.setOnClickListener { listener.onItemClick(lugar) }

                placeImage1.visibility = View.VISIBLE
                placeName1.visibility = View.VISIBLE
            } else {
                placeImage1.setImageDrawable(null)
                placeName1.text = ""
                placeImage1.visibility = View.GONE
                placeName1.visibility = View.GONE
            }
        }

        fun bindPlace2(lugar: Lugar?) {
            if (lugar != null) {
                Glide.with(context)
                    .load(lugar.imagenUrl)
                    .into(placeImage2)

                placeName2.text = lugar.nombre

                placeImage2.setOnClickListener { listener.onItemClick(lugar) }
                placeName2.setOnClickListener { listener.onItemClick(lugar) }

                placeImage2.visibility = View.VISIBLE
                placeName2.visibility = View.VISIBLE
            } else {
                placeImage2.setImageDrawable(null)
                placeName2.text = ""
                placeImage2.visibility = View.GONE
                placeName2.visibility = View.GONE
            }
        }
    }
}
