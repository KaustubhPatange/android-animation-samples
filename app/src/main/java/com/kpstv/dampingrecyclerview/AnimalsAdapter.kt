package com.kpstv.dampingrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

data class Animal(@DrawableRes val drawableRes: Int, val name: String)

class AnimalsAdapter : RecyclerView.Adapter<AnimalsAdapter.AnimalHolder>() {
     private val list: List<Animal>
     init {
         list = getFakeData()
     }

    class AnimalHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(model: Animal) {
            view.findViewById<TextView>(R.id.textView).text = model.name
            view.findViewById<ImageView>(R.id.imageView).setImageResource(model.drawableRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalHolder =
        AnimalHolder(LayoutInflater.from(parent.context).inflate(R.layout.base_item, parent, false))

    override fun onBindViewHolder(holder: AnimalHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    private fun getFakeData(): List<Animal> {
        // Hacky reflection to get data
        val list = ArrayList<Animal>()
        R.drawable::class.java.fields.forEach {
            if (it.name.startsWith("icon_")) {
                val animal = Animal(it.getInt(this), it.name.removePrefix("icon_").replace("_"," ").capitalizeWords())
                list.add(animal)
            }
        }
        return list
    }
}

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }