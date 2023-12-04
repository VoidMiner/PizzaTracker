package com.example.pizzatracker

import java.io.Serializable

data class Dish(val name: String, val details: String? = null) : Serializable
