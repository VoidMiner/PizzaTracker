package com.example.pizzatracker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATION")
class CategoryAdapter(
    private val categories: List<MenuCategory>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(category: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val category = categories[position]
                    onItemClickListener.onItemClick(category.name)
                    Log.d("CategoryAdapter", "Item clicked at position $position, Category: $category")

                }
            }
        }

        fun bind(category: MenuCategory) {
            categoryNameTextView.text = category.name
        }
    }
}
