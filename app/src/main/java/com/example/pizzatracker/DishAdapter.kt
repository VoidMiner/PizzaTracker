package com.example.pizzatracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DishAdapter(private var dishes: List<Dish>, param: (Any) -> Unit) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dish = dishes[position]
        holder.bind(dish)
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun updateDishes(newDishes: List<Dish>) {
        dishes = newDishes
        notifyDataSetChanged()
    }

    inner class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dishNameTextView: TextView = itemView.findViewById(R.id.dishNameTextView)
        private val dishDetailsTextView: TextView = itemView.findViewById(R.id.dishDetailsTextView)

        fun bind(dish: Dish) {
            dishNameTextView.text = dish.name
            dishDetailsTextView.text = dish.details
        }
    }
}


