package com.example.pizzatracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryFragment : Fragment() {

    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): CategoryFragment {
            val fragment = CategoryFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var category: String
    private lateinit var dishAdapter: DishAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(ARG_CATEGORY, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("CategoryFragment", "onCreateView")

        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val categoryNameTextView = view.findViewById<TextView>(R.id.categoryNameTextView)
        categoryNameTextView.text = category

        val dishRecyclerView = view.findViewById<RecyclerView>(R.id.dishRecyclerView)
        dishAdapter = DishAdapter(createDummyDishes()) { _ ->
        }
        dishRecyclerView.adapter = dishAdapter
        dishRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    private fun createDummyDishes(): List<Dish> {
        val dishes = mutableListOf<Dish>()
        for (j in 1..30) {
            dishes.add(Dish("Блюдо $j"))
        }
        return dishes
    }
}