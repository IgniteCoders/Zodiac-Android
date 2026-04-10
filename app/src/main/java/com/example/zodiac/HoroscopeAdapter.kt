package com.example.zodiac

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HoroscopeAdapter(val items: List<Horoscope>) : RecyclerView.Adapter<HoroscopeViewHolder>() {

    // Cual es la vista para los elementos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope, parent, false)
        return HoroscopeViewHolder(view)
    }

    // Cual son los datos a mostrar para el elemento en la posicion
    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val horoscope = items[position]
        holder.render(horoscope)
    }

    // Cuantos elementos tengo que mostrar
    override fun getItemCount(): Int {
        return items.size
    }

}

class HoroscopeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val signImageView: ImageView = itemView.findViewById(R.id.signImageView)
    val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    val datesTextView: TextView = itemView.findViewById(R.id.datesTextView)

    fun render(horoscope: Horoscope) {
        nameTextView.setText(horoscope.name)
        datesTextView.setText(horoscope.dates)
        signImageView.setImageResource(horoscope.image)
    }
}