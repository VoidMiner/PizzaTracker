package com.example.pizzatracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuFragment : Fragment(), CategoryAdapter.OnItemClickListener {

    private val menuCategories = createDummyMenuCategories()

    private lateinit var categoryAdapter: CategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MenuFragment", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        val categoryRecyclerView: RecyclerView = view.findViewById(R.id.categoryRecyclerView)

        categoryAdapter = CategoryAdapter(menuCategories, this)
        categoryRecyclerView.adapter = categoryAdapter
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }
    override fun onItemClick(category: String) {
        Log.d("MenuFragment", "Clicked on category: $category")
        val categoryFragment = CategoryFragment.newInstance(category)
        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, categoryFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun createDummyMenuCategories(): List<MenuCategory> {
        val menuCategories = mutableListOf<MenuCategory>()
        for (i in 1..30) {
            menuCategories.add(MenuCategory("Категория $i", createDummyDishes()))
        }
        return menuCategories
    }

    private fun createDummyDishes(): List<Dish> {
        val dishes = mutableListOf<Dish>()
        for (j in 1..30) {
            dishes.add(Dish("Блюдо $j", "Описание блюда $j"))
        }
        return dishes
    }
}