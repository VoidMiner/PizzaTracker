package com.example.pizzatracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class DishCardFragment : Fragment() {

    companion object {
        private const val ARG_DISH = "dish"

        fun newInstance(dish: Dish): DishCardFragment {
            val fragment = DishCardFragment()
            val args = Bundle()
            args.putSerializable(ARG_DISH, dish)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dish_card, container, false)

        val dish = arguments?.getSerializable(ARG_DISH) as Dish
        val dishImageView: ImageView = view.findViewById(R.id.dishImageView)

        val dishNameTextView: TextView = view.findViewById(R.id.dishNameTextView)
        dishNameTextView.text = dish.name

        val dishDetailsTextView: TextView = view.findViewById(R.id.dishDetailsTextView)

        val detailsTemplate = getString(R.string.dish_details_template)

        dishDetailsTextView.text = String.format(detailsTemplate, dish.details)

        return view
    }
}
