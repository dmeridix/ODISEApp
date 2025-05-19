package com.example.odisea.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.R


class ReservationAdapter(
    private val reservations: List<Map<String, String>>
) : RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_reservation_title)
        val tvDetails: TextView = view.findViewById(R.id.tv_reservation_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reservation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.tvTitle.text = reservation["title"]
        holder.tvDetails.text = reservation["details"]
    }

    override fun getItemCount(): Int = reservations.size
}