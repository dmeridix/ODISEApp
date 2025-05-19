package com.example.odisea.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.R
import com.example.odisea.data.Reserva

class ReservationAdapter(
    private val reservas: List<Reserva>,
    private val onCancelClick: (Reserva) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tipoText: TextView = view.findViewById(R.id.tv_tipo_reserva)
        private val nombreText: TextView = view.findViewById(R.id.tv_nombre_lugar)
        private val fechaText: TextView = view.findViewById(R.id.tv_fecha_reserva)
        private val cancelBtn: Button = view.findViewById(R.id.btn_cancelar_reserva)

        fun bind(reserva: Reserva) {
            tipoText.text = reserva.tipo
            nombreText.text = reserva.nombreLugar
            fechaText.text = reserva.fecha
            cancelBtn.setOnClickListener { onCancelClick(reserva) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reservas[position])
    }

    override fun getItemCount(): Int = reservas.size
}